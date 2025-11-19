package ar.edu.um.programacion2.marcos2504.EventosProxy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

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
}
