package ar.edu.um.programacion2.marcosibarra.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para comunicarse con el EventosProxy
 * El proxy maneja la autenticación con cátedra automáticamente
 */
@FeignClient(
    name = "eventos-proxy",
    url = "${proxy.base-url}"
)
public interface ProxyClient {

    /**
     * GET /eventos/resumidos
     * Lista resumida de eventos activos
     */
    @GetMapping("/eventos/resumidos")
    List<Map<String, Object>> getEventosResumidos();

    /**
     * GET /eventos
     * Lista completa de eventos con todos los datos
     */
    @GetMapping("/eventos")
    List<Map<String, Object>> getEventos();

    /**
     * GET /eventos/{id}
     * Detalle completo de un evento específico
     */
    @GetMapping("/eventos/{id}")
    Map<String, Object> getEvento(@PathVariable("id") Long id);

    /**
     * POST /eventos/bloquear-asientos
     * Bloquea asientos temporalmente (5 minutos)
     *
     * Request: {"eventoId": 1, "asientos": [{"fila": 2, "columna": 1}]}
     */
    @PostMapping("/eventos/bloquear-asientos")
    Map<String, Object> bloquearAsientos(@RequestBody Map<String, Object> request);

    /**
     * POST /eventos/realizar-venta
     * Realiza la venta de asientos
     *
     * Request: {"eventoId": 1, "fecha": "...", "precioVenta": 5000,
     *           "asientos": [{"fila": 2, "columna": 1, "persona": "Juan"}]}
     */
    @PostMapping("/eventos/realizar-venta")
    Map<String, Object> realizarVenta(@RequestBody Map<String, Object> request);

    /**
     * GET /asientos/{eventoId}/{fila}/{columna}
     * Consulta el estado de un asiento específico en Redis de cátedra
     *
     * Response: "Libre" | "Ocupado" | "Bloqueado" | etc.
     */
    @GetMapping("/asientos/{eventoId}/{fila}/{columna}")
    String getEstadoAsiento(
        @PathVariable("eventoId") Long eventoId,
        @PathVariable("fila") Integer fila,
        @PathVariable("columna") Integer columna
    );

    /**
     * GET /eventos/ventas
     * Lista todas las ventas del alumno
     */
    @GetMapping("/eventos/ventas")
    List<Map<String, Object>> getVentas();

    /**
     * GET /eventos/ventas/{id}
     * Detalle de una venta específica
     */
    @GetMapping("/eventos/ventas/{id}")
    Map<String, Object> getVenta(@PathVariable("id") Long id);
}
