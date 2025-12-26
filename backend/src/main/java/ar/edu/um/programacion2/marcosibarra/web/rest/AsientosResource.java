package ar.edu.um.programacion2.marcosibarra.web.rest;

import ar.edu.um.programacion2.marcosibarra.client.ProxyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller para consultar asientos desde Redis vía proxy.
 * NO almacena estado de asientos - Redis es la fuente de verdad.
 */
@RestController
@RequestMapping("/api")
public class AsientosResource {

    private static final Logger log = LoggerFactory.getLogger(AsientosResource.class);

    private final ProxyClient proxyClient;

    public AsientosResource(ProxyClient proxyClient) {
        this.proxyClient = proxyClient;
    }

    /**
     * GET /asientos/evento/{eventoId} : Obtiene los asientos de un evento desde Redis
     *
     * @param eventoId ID del evento
     * @return Estado de los asientos (LIBRE, BLOQUEADO, VENDIDO)
     */
    @GetMapping("/asientos/evento/{eventoId}")
    public ResponseEntity<List<Map<String, Object>>> getAsientosPorEvento(@PathVariable Long eventoId) {
        log.info("REST request para obtener asientos del evento ID: {}", eventoId);

        try {
            // Construir la clave que usa Redis (formato: "evento_ID")
            String clave = "evento_" + eventoId;
            Map<String, Object> response = proxyClient.getAsientosPorClave(clave);

            // Extraer la lista de asientos del response
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> asientos = (List<Map<String, Object>>) response.get("asientos");

            return ResponseEntity.ok(asientos);
        } catch (Exception e) {
            log.error("Error obteniendo asientos del evento {}", eventoId, e);
            throw new RuntimeException("Error al obtener asientos: " + e.getMessage());
        }
    }
}
