package ar.edu.um.programacion2.marcosibarra.service;

import ar.edu.um.programacion2.marcosibarra.client.ProxyClient;
import ar.edu.um.programacion2.marcosibarra.domain.Evento;
import ar.edu.um.programacion2.marcosibarra.domain.EventoTipo;
import ar.edu.um.programacion2.marcosibarra.repository.EventoRepository;
import ar.edu.um.programacion2.marcosibarra.repository.EventoTipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Servicio para sincronizar eventos desde la cátedra (vía proxy)
 * Actualiza la base de datos local con los eventos del servidor
 */
@Service
public class EventoSyncService {

    private static final Logger log = LoggerFactory.getLogger(EventoSyncService.class);

    private final ProxyClient proxyClient;
    private final EventoRepository eventoRepository;
    private final EventoTipoRepository eventoTipoRepository;

    public EventoSyncService(
        ProxyClient proxyClient,
        EventoRepository eventoRepository,
        EventoTipoRepository eventoTipoRepository
    ) {
        this.proxyClient = proxyClient;
        this.eventoRepository = eventoRepository;
        this.eventoTipoRepository = eventoTipoRepository;
    }

    /**
     * Sincroniza eventos automáticamente cada 5 minutos
     * Se ejecuta 10 segundos después de iniciar la aplicación
     *
     * DESACTIVADO: Se activará cuando se implemente el consumer de Kafka
     */
    // @Scheduled(fixedDelay = 300000, initialDelay = 10000) // 5 minutos
    public void sincronizarEventosAutomatico() {
        try {
            log.info("Iniciando sincronización automática de eventos...");
            sincronizarTodosLosEventos();
        } catch (Exception e) {
            log.error("Error en sincronización automática de eventos", e);
        }
    }

    /**
     * Sincroniza todos los eventos desde la cátedra
     */
    public void sincronizarTodosLosEventos() {
        List<Map<String, Object>> eventosCatedra = proxyClient.getEventos();
        log.info("Obtenidos {} eventos de cátedra", eventosCatedra.size());

        int creados = 0;
        int actualizados = 0;

        for (Map<String, Object> eventoCatedra : eventosCatedra) {
            try {
                boolean esNuevo = sincronizarEvento(eventoCatedra);
                if (esNuevo) {
                    creados++;
                } else {
                    actualizados++;
                }
            } catch (Exception e) {
                log.error("Error sincronizando evento: {}", eventoCatedra.get("id"), e);
            }
        }

        log.info("Sincronización completada: {} eventos creados, {} actualizados", creados, actualizados);
    }

    /**
     * Sincroniza un evento específico desde la cátedra
     * @return true si el evento es nuevo, false si ya existía
     */
    public boolean sincronizarEvento(Map<String, Object> eventoCatedra) {
        Long idCatedra = getLongValue(eventoCatedra.get("id"));

        // 1. Buscar o crear evento (versión simplificada)
        Evento evento = eventoRepository.findByIdCatedra(idCatedra)
            .orElseGet(() -> {
                Evento nuevo = new Evento();
                nuevo.setIdCatedra(idCatedra);
                log.debug("Creando nuevo evento: {}", idCatedra);
                return nuevo;
            });

        // Verificar si es nuevo ANTES de guardar
        boolean esNuevo = evento.getId() == null;

        // Si ya existía, lo actualizamos
        if (!esNuevo) {
            log.debug("Actualizando evento existente: {}", idCatedra);
        }

        // 2. Mapear datos desde cátedra
        mapearDatosCatedra(evento, eventoCatedra);

        // 3. Mapear tipo de evento (buscar o crear)
        mapearTipoEvento(evento, eventoCatedra);

        // 4. Guardar
        eventoRepository.save(evento);

        // TODO: Sincronizar integrantes si es necesario

        return esNuevo;
    }

    /**
     * Mapea los datos básicos de cátedra a la entidad
     */
    private void mapearDatosCatedra(Evento evento, Map<String, Object> eventoCatedra) {
        evento.setTitulo((String) eventoCatedra.get("titulo"));
        evento.setResumen((String) eventoCatedra.get("resumen"));
        evento.setDescripcion((String) eventoCatedra.get("descripcion"));
        evento.setFecha(Instant.parse((String) eventoCatedra.get("fecha")));
        evento.setDireccion((String) eventoCatedra.get("direccion"));
        evento.setImagen((String) eventoCatedra.get("imagen"));

        // Asientos
        evento.setFilaAsientos(getIntegerValue(eventoCatedra.get("filaAsientos")));
        evento.setColumnaAsientos(getIntegerValue(eventoCatedra.get("columnAsientos")));

        // Precio
        Object precioObj = eventoCatedra.get("precioEntrada");
        if (precioObj != null) {
            evento.setPrecioEntrada(new BigDecimal(precioObj.toString()));
        }
    }

    /**
     * Mapea el tipo de evento (busca existente o crea nuevo)
     */
    private void mapearTipoEvento(Evento evento, Map<String, Object> eventoCatedra) {
        @SuppressWarnings("unchecked")
        Map<String, Object> tipoData = (Map<String, Object>) eventoCatedra.get("eventoTipo");

        if (tipoData == null) {
            return;
        }

        String nombreTipo = (String) tipoData.get("nombre");

        // Buscar tipo existente o crear nuevo
        EventoTipo tipo = eventoTipoRepository.findByNombre(nombreTipo)
            .orElseGet(() -> crearNuevoTipo(nombreTipo, (String) tipoData.get("descripcion")));

        evento.setTipo(tipo);
    }

    /**
     * Crea un nuevo tipo de evento
     */
    private EventoTipo crearNuevoTipo(String nombre, String descripcion) {
        EventoTipo nuevoTipo = new EventoTipo();
        nuevoTipo.setNombre(nombre);
        nuevoTipo.setDescripcion(descripcion);
        return eventoTipoRepository.save(nuevoTipo);
    }

    /**
     * Sincroniza un evento específico por su ID de cátedra
     */
    public void sincronizarEventoPorId(Long idCatedra) {
        try {
            log.info("Sincronizando evento específico: {}", idCatedra);
            Map<String, Object> eventoCatedra = proxyClient.getEvento(idCatedra);
            sincronizarEvento(eventoCatedra);
        } catch (Exception e) {
            log.error("Error sincronizando evento {}", idCatedra, e);
            throw e;
        }
    }

    private Long getLongValue(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        return Long.parseLong(value.toString());
    }

    private Integer getIntegerValue(Object value) {
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Long) return ((Long) value).intValue();
        return Integer.parseInt(value.toString());
    }
}
