package ar.edu.um.programacion2.marcosibarra.service;

import ar.edu.um.programacion2.marcosibarra.domain.Sesion;
import ar.edu.um.programacion2.marcosibarra.domain.User;
import ar.edu.um.programacion2.marcosibarra.domain.enumeration.EstadoSesion;
import ar.edu.um.programacion2.marcosibarra.repository.SesionRepository;
import ar.edu.um.programacion2.marcosibarra.service.dto.SesionDTO;
import ar.edu.um.programacion2.marcosibarra.service.mapper.SesionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service para gestión de sesiones de usuario
 * Maneja el estado del flujo de compra (evento -> asientos -> datos -> venta)
 */
@Service
@Transactional
public class SesionManagementService {

    private static final Logger log = LoggerFactory.getLogger(SesionManagementService.class);

    private final SesionRepository sesionRepository;
    private final SesionMapper sesionMapper;

    public SesionManagementService(SesionRepository sesionRepository, SesionMapper sesionMapper) {
        this.sesionRepository = sesionRepository;
        this.sesionMapper = sesionMapper;
    }

    /**
     * Obtiene la sesión activa del usuario o crea una nueva si no existe
     * Este método se llama al login para retomar el estado donde quedó
     */
    public SesionDTO obtenerOCrearSesion(User usuario) {
        log.info("Buscando sesión activa para usuario: {}", usuario.getLogin());

        // Buscar sesión activa existente
        Optional<Sesion> sesionActiva = sesionRepository.findByUsuarioAndActivaTrue(usuario);

        if (sesionActiva.isPresent()) {
            log.info("Sesión activa encontrada, retomando estado: {}", sesionActiva.get().getEstadoSesion());
            Sesion sesion = sesionActiva.get();
            sesion.setUltimoAcceso(Instant.now());
            return sesionMapper.toDto(sesionRepository.save(sesion));
        }

        // Crear nueva sesión
        log.info(" Creando nueva sesión para usuario: {}", usuario.getLogin());
        Sesion nueva = new Sesion();
        nueva.setUsuario(usuario);
        nueva.setTokenJWT(""); // Se actualiza después si es necesario
        nueva.setFechaInicio(Instant.now());
        nueva.setActiva(true);
        nueva.setUltimoAcceso(Instant.now());
        nueva.setEstadoSesion(EstadoSesion.LISTA_EVENTOS);

        return sesionMapper.toDto(sesionRepository.save(nueva));
    }

    /**
     * Actualiza el estado de sesión cuando el usuario selecciona un evento
     */
    public SesionDTO seleccionarEvento(User usuario, Long eventoId) {
        log.info(" Usuario {} seleccionó evento: {}", usuario.getLogin(), eventoId);

        Sesion sesion = obtenerSesionActiva(usuario);

        sesion.setEventoSeleccionado(eventoId);
        sesion.setEstadoSesion(EstadoSesion.EVENTO_SELECCIONADO);
        sesion.setUltimoAcceso(Instant.now());

        return sesionMapper.toDto(sesionRepository.save(sesion));
    }

    /**
     * Actualiza el estado cuando el usuario bloquea asientos
     */
    public SesionDTO bloquearAsientos(User usuario, String asientosJson, Integer cantidad) {
        log.info(" Usuario {} bloqueó {} asientos", usuario.getLogin(), cantidad);

        Sesion sesion = obtenerSesionActiva(usuario);

        sesion.setAsientosSeleccionados(asientosJson);
        sesion.setCantidadAsientos(cantidad);
        sesion.setEstadoSesion(EstadoSesion.ASIENTOS_BLOQUEADOS);
        sesion.setUltimoAcceso(Instant.now());

        return sesionMapper.toDto(sesionRepository.save(sesion));
    }

    /**
     * Actualiza el estado cuando el usuario completa los datos de los asientos
     */
    public SesionDTO completarDatos(User usuario) {
        log.info("✍️ Usuario {} completó datos de asientos", usuario.getLogin());

        Sesion sesion = obtenerSesionActiva(usuario);

        sesion.setEstadoSesion(EstadoSesion.DATOS_COMPLETOS);
        sesion.setUltimoAcceso(Instant.now());

        return sesionMapper.toDto(sesionRepository.save(sesion));
    }



    /**
     * Invalida la sesión actual del usuario (logout)
     */
    public void invalidarSesion(User usuario) {
        log.info("🚪 Invalidando sesión para usuario: {}", usuario.getLogin());

        sesionRepository.findByUsuarioAndActivaTrue(usuario)
            .ifPresent(sesion -> {
                sesion.setActiva(false);
                sesionRepository.save(sesion);
                log.info("Sesión invalidada exitosamente");
            });
    }

    /**
     * Obtiene la sesión activa del usuario o lanza excepción si no existe
     */
    private Sesion obtenerSesionActiva(User usuario) {
        return sesionRepository.findByUsuarioAndActivaTrue(usuario)
            .orElseThrow(() -> new RuntimeException("No hay sesión activa para el usuario: " + usuario.getLogin()));
    }
    public SesionDTO retroceder(User usuario) {
        log.info("⬅️ Usuario {} retrocediendo en el flujo", usuario.getLogin());

        Sesion sesion = obtenerSesionActiva(usuario);
        EstadoSesion estadoActual = sesion.getEstadoSesion();

        log.info("Estado actual: {}", estadoActual);

        switch (estadoActual) {
            case VENTA_CONFIRMADA:
                // Retroceder a DATOS_COMPLETOS
                sesion.setEstadoSesion(EstadoSesion.DATOS_COMPLETOS);
                log.info("➡️ Retrocediendo a DATOS_COMPLETOS");
                break;

            case DATOS_COMPLETOS:
                // Retroceder a ASIENTOS_BLOQUEADOS
                sesion.setEstadoSesion(EstadoSesion.ASIENTOS_BLOQUEADOS);
                log.info("➡️ Retrocediendo a ASIENTOS_BLOQUEADOS");
                break;

            case ASIENTOS_BLOQUEADOS:
                // Retroceder a EVENTO_SELECCIONADO
                // Limpiar asientos bloqueados
                sesion.setEstadoSesion(EstadoSesion.EVENTO_SELECCIONADO);
                sesion.setAsientosSeleccionados(null);
                sesion.setCantidadAsientos(0);
                log.info("➡️ Retrocediendo a EVENTO_SELECCIONADO (asientos liberados)");
                // TODO: Liberar asientos en Redis si aplica
                break;

            case EVENTO_SELECCIONADO:
                // Retroceder a LISTA_EVENTOS
                sesion.setEstadoSesion(EstadoSesion.LISTA_EVENTOS);
                sesion.setEventoSeleccionado(null);
                log.info("➡️ Retrocediendo a LISTA_EVENTOS (evento deseleccionado)");
                break;

            case LISTA_EVENTOS:
                // Ya está en el inicio, no retroceder más
                log.info("ℹ️ Ya está en LISTA_EVENTOS, no se puede retroceder más");
                break;

            case EXPIRADA:
                // Sesión expirada, resetear a LISTA_EVENTOS
                sesion.setEstadoSesion(EstadoSesion.LISTA_EVENTOS);
                sesion.setEventoSeleccionado(null);
                sesion.setAsientosSeleccionados(null);
                sesion.setCantidadAsientos(0);
                log.info("➡️ Sesión expirada, reseteando a LISTA_EVENTOS");
                break;

            default:
                log.warn("⚠️ Estado desconocido: {}", estadoActual);
                break;
        }

        sesion.setUltimoAcceso(Instant.now());
        Sesion sesionActualizada = sesionRepository.save(sesion);

        log.info("✅ Sesión actualizada a estado: {}", sesionActualizada.getEstadoSesion());

        return sesionMapper.toDto(sesionActualizada);
    }
}
