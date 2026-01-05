package ar.edu.um.programacion2.marcos2504.EventosProxy.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * Cliente Feign para comunicarse con el Backend
 */
@FeignClient(
        name = "backend-client",
        url = "${eventos.backend.url}"
)
public interface BackendClient {

    /**
     * Notifica al backend que debe sincronizar eventos
     * Llamado automáticamente cuando llega un mensaje de Kafka
     */
    @PostMapping("${eventos.backend.sync-endpoint}")
    Map<String, Object> triggerEventoSync();
}
