package ar.edu.um.programacion2.marcosibarra.service;

import ar.edu.um.programacion2.marcosibarra.client.ProxyClient;
import ar.edu.um.programacion2.marcosibarra.domain.*;
import ar.edu.um.programacion2.marcosibarra.domain.enumeration.*;
import ar.edu.um.programacion2.marcosibarra.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

/**
 * Servicio para procesar bloqueos y ventas de asientos.
 * NO almacena estado de asientos localmente - Redis (vía proxy) es la fuente de verdad.
 */
@Service
@Transactional
public class VentaProcessService {

    private static final Logger log = LoggerFactory.getLogger(VentaProcessService.class);

    private final ProxyClient proxyClient;
    private final VentaRepository ventaRepository;
    private final EventoRepository eventoRepository;
    private final UserRepository userRepository;

    public VentaProcessService(
        ProxyClient proxyClient,
        VentaRepository ventaRepository,
        EventoRepository eventoRepository,
        UserRepository userRepository
    ) {
        this.proxyClient = proxyClient;
        this.ventaRepository = ventaRepository;
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
    }

    /**
     * Bloquea asientos para un evento.
     * Delega directamente al proxy (Redis).
     */
    public Map<String, Object> bloquearAsientos(Long eventoId, List<Map<String, Integer>> asientosCoord, String username) {
        log.info("Bloqueando {} asientos para evento {} por usuario {}",
            asientosCoord.size(), eventoId, username);

        Map<String, Object> request = new HashMap<>();
        request.put("eventoId", eventoId);
        request.put("asientos", asientosCoord);

        try {
            Map<String, Object> responseProxy = proxyClient.bloquearAsientos(request);
            log.info("Respuesta del proxy al bloquear asientos: {}", responseProxy);

            // Redis es la fuente de verdad - no actualizamos localmente
            return responseProxy;
        } catch (Exception e) {
            log.error("Error bloqueando asientos", e);
            throw new RuntimeException("Error al bloquear asientos: " + e.getMessage());
        }
    }

    /**
     * Realiza la venta de asientos.
     * ANTES de vender, verifica que los asientos sigan bloqueados en Redis.
     * Notifica al proxy (Redis) y solo almacena el registro de venta localmente.
     */
    public Venta realizarVenta(Long eventoId, List<Map<String, Object>> asientosVenta, String username) {
        log.info("Realizando venta de {} asientos para evento {} por usuario {}",
            asientosVenta.size(), eventoId, username);

        Evento evento = eventoRepository.findById(eventoId)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado: " + eventoId));

        User usuario = userRepository.findOneByLogin(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));

        BigDecimal precioTotal = evento.getPrecioEntrada().multiply(BigDecimal.valueOf(asientosVenta.size()));

        try {
            // 1. VERIFICAR ESTADO ACTUAL desde Redis antes de vender
            String clave = "evento_" + eventoId;
            Map<String, Object> response = proxyClient.getAsientosPorClave(clave);

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> asientosRedis = (List<Map<String, Object>>) response.get("asientos");

            // Validar que todos los asientos estén bloqueados
            for (Map<String, Object> asientoVenta : asientosVenta) {
                int fila = ((Number) asientoVenta.get("fila")).intValue();
                int columna = ((Number) asientoVenta.get("columna")).intValue();

                boolean bloqueado = asientosRedis.stream()
                    .anyMatch(a ->
                        ((Number) a.get("fila")).intValue() == fila &&
                            ((Number) a.get("columna")).intValue() == columna &&
                            "BLOQUEADO".equalsIgnoreCase((String) a.get("estado"))
                    );

                if (!bloqueado) {
                    log.warn("Asiento ({},{}) no está bloqueado. Venta rechazada.", fila, columna);

                    Venta ventaRechazada = new Venta();
                    ventaRechazada.setFechaVenta(Instant.now());
                    ventaRechazada.setPrecioVenta(precioTotal);
                    ventaRechazada.setCantidadAsientos(asientosVenta.size());
                    ventaRechazada.setEvento(evento);
                    ventaRechazada.setUsuario(usuario);
                    ventaRechazada.setResultado(false);
                    ventaRechazada.setEstadoVenta(EstadoVenta.RECHAZADA);
                    ventaRechazada.setDescripcion("El asiento (" + fila + "," + columna + ") ya no está bloqueado. Puede haber expirado el tiempo.");

                    return ventaRepository.save(ventaRechazada);
                }
            }

            log.info("Todos los asientos están bloqueados. Procediendo con la venta...");

            // 2. Proceder con la venta
            Map<String, Object> request = new HashMap<>();
            request.put("eventoId", eventoId);
            request.put("fecha", Instant.now().toString());
            request.put("precioVenta", precioTotal);
            request.put("asientos", asientosVenta);

            // Notificar venta al proxy - Redis maneja el estado de asientos
            Map<String, Object> responseProxy = proxyClient.realizarVenta(request);
            log.info("Respuesta del proxy al realizar venta: {}", responseProxy);

            // Solo guardamos el registro de venta localmente
            Venta venta = new Venta();
            venta.setFechaVenta(Instant.now());
            venta.setPrecioVenta(precioTotal);
            venta.setCantidadAsientos(asientosVenta.size());
            venta.setEvento(evento);
            venta.setUsuario(usuario);

            Boolean resultado = (Boolean) responseProxy.get("resultado");

            if (Boolean.TRUE.equals(resultado)) {
                venta.setResultado(true);
                venta.setEstadoVenta(EstadoVenta.CONFIRMADA);
                venta.setDescripcion("Venta realizada exitosamente");
            } else {
                venta.setResultado(false);
                venta.setEstadoVenta(EstadoVenta.RECHAZADA);
                String descripcion = (String) responseProxy.getOrDefault("descripcion", "Venta rechazada");
                venta.setDescripcion(descripcion);
            }

            return ventaRepository.save(venta);
        } catch (Exception e) {
            log.error("Error realizando venta", e);

            // Registrar venta con error
            Venta ventaError = new Venta();
            ventaError.setFechaVenta(Instant.now());
            ventaError.setPrecioVenta(precioTotal);
            ventaError.setCantidadAsientos(asientosVenta.size());
            ventaError.setEvento(evento);
            ventaError.setUsuario(usuario);
            ventaError.setResultado(false);
            ventaError.setEstadoVenta(EstadoVenta.ERROR_SINCRONIZACION);
            ventaError.setDescripcion("Error: " + e.getMessage());

            return ventaRepository.save(ventaError);
        }
    }
}

