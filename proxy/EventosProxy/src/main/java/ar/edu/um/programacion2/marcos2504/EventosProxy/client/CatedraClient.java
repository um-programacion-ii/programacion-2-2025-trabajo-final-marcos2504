package ar.edu.um.programacion2.marcos2504.EventosProxy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "catedraClient",
        url = "${catedra.base-url}"
)
public interface CatedraClient {

    @GetMapping("/eventos-resumidos")
    List<Object> getEventosResumidos(
            @RequestHeader("Authorization") String auth
    );

    @GetMapping("/eventos")
    List<Object> getEventos(
            @RequestHeader("Authorization") String auth
    );

    @GetMapping("/evento/{id}")
    Object getEvento(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth
    );

    @PostMapping("/bloquear-asientos")
    Object bloquearAsientos(
            @RequestBody Object request,
            @RequestHeader("Authorization") String auth
    );

    @PostMapping("/realizar-venta")
    Object realizarVenta(
            @RequestBody Object request,
            @RequestHeader("Authorization") String auth
    );

    @GetMapping("/listar-ventas")
    List<Object> listarVentas(
            @RequestHeader("Authorization") String auth
    );

    @GetMapping("/listar-venta/{id}")
    Object listarVenta(
            @PathVariable Long id,
            @RequestHeader("Authorization") String auth
    );
}
