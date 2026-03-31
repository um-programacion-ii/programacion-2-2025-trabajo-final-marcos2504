
package ar.edu.um.programacion2.marcosibarra.web.rest;

import ar.edu.um.programacion2.marcosibarra.service.EventoSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller para sincronización manual de eventos
 * Útil para testing y sincronización on-demand
 */
@RestController
@RequestMapping("/api/sync")
public class EventoSyncResource {

    private static final Logger log = LoggerFactory.getLogger(EventoSyncResource.class);

    private final EventoSyncService eventoSyncService;

    public EventoSyncResource(EventoSyncService eventoSyncService) {
        this.eventoSyncService = eventoSyncService;
    }

    /**
     * POST /api/sync/eventos : Sincroniza todos los eventos desde cátedra
     *
     * @return Resultado de la sincronización
     */
    @PostMapping("/eventos")
    public ResponseEntity<Map<String, Object>> sincronizarTodosLosEventos() {
        log.info("REST request para sincronizar todos los eventos");

        try {
            eventoSyncService.sincronizarTodosLosEventos();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sincronización completada exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sincronizando eventos", e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error en sincronización: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * POST /api/sync/eventos/{id} : Sincroniza un evento específico
     *
     * @param id ID del evento en cátedra
     * @return Resultado de la sincronización
     */
    @PostMapping("/eventos/{id}")
    public ResponseEntity<Map<String, Object>> sincronizarEventoPorId(@PathVariable Long id) {
        log.info("REST request para sincronizar evento: {}", id);

        try {
            eventoSyncService.sincronizarEventoPorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Evento " + id + " sincronizado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error sincronizando evento {}", id, e);

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error sincronizando evento: " + e.getMessage());

            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * GET /api/sync/test : Verifica que el proxy esté accesible
     *
     * @return Estado de conexión con el proxy
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> testConexionProxy() {
        log.info("REST request para probar conexión con proxy");

        Map<String, Object> response = new HashMap<>();
        response.put("service", "EventoSyncService");
        response.put("status", "running");
        response.put("message", "Servicio de sincronización disponible");

        return ResponseEntity.ok(response);
    }
}
