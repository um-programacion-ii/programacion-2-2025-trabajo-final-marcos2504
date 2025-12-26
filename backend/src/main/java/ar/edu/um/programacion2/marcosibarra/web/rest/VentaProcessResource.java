package ar.edu.um.programacion2.marcosibarra.web.rest;

import ar.edu.um.programacion2.marcosibarra.domain.Venta;
import ar.edu.um.programacion2.marcosibarra.security.SecurityUtils;
import ar.edu.um.programacion2.marcosibarra.service.VentaProcessService;
import ar.edu.um.programacion2.marcosibarra.service.dto.VentaDTO;
import ar.edu.um.programacion2.marcosibarra.service.mapper.VentaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller para el proceso de compra de asientos
 */
@RestController
@RequestMapping("/api/compras")
public class VentaProcessResource {

    private static final Logger log = LoggerFactory.getLogger(VentaProcessResource.class);

    private final VentaProcessService ventaProcessService;
    private final VentaMapper ventaMapper;

    public VentaProcessResource(VentaProcessService ventaProcessService, VentaMapper ventaMapper) {
        this.ventaProcessService = ventaProcessService;
        this.ventaMapper = ventaMapper;
    }

    /**
     * POST /ventas/bloquear : Bloquea asientos antes de la venta
     * <p>
     * Body: {
     * "eventoId": 1,
     * "asientos": [
     * {"fila": 2, "columna": 1},
     * {"fila": 2, "columna": 2}
     * ]
     * }
     *
     * @return Resultado del bloqueo
     */
    @PostMapping("/bloquear")
    public ResponseEntity<Map<String, Object>> bloquearAsientos(@RequestBody Map<String, Object> request) {
        log.info("REST request para bloquear asientos");

        String username = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        Long eventoId = ((Number) request.get("eventoId")).longValue();

        @SuppressWarnings("unchecked")
        List<Map<String, Integer>> asientos = (List<Map<String, Integer>>) request.get("asientos");

        Map<String, Object> resultado = ventaProcessService.bloquearAsientos(eventoId, asientos, username);

        return ResponseEntity.ok(resultado);
    }

    /**
     * POST /ventas/realizar : Realiza la venta de asientos bloqueados
     * <p>
     * Body: {
     * "eventoId": 1,
     * "asientos": [
     * {"fila": 2, "columna": 1, "persona": "Juan Perez"},
     * {"fila": 2, "columna": 2, "persona": "Maria Lopez"}
     * ]
     * }
     *
     * @return Venta creada
     */
    @PostMapping("/realizar")
    public ResponseEntity<VentaDTO> realizarVenta(@RequestBody Map<String, Object> request) {
        log.info("REST request para realizar venta");

        String username = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        Long eventoId = ((Number) request.get("eventoId")).longValue();

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> asientos = (List<Map<String, Object>>) request.get("asientos");

        Venta venta = ventaProcessService.realizarVenta(eventoId, asientos, username);
        VentaDTO ventaDTO = ventaMapper.toDto(venta);

        return ResponseEntity.ok(ventaDTO);
    }
}
