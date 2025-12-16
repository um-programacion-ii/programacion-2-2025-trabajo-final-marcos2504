package ar.edu.um.programacion2.marcos2504.EventosProxy.controller;

import ar.edu.um.programacion2.marcos2504.EventosProxy.service.AsientoRedisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proxy/asientos")
public class AsientoProxyController {

    private final AsientoRedisService redisService;

    public AsientoProxyController(AsientoRedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/{eventoId}/{fila}/{columna}")
    public Object obtenerEstado(
            @PathVariable Long eventoId,
            @PathVariable int fila,
            @PathVariable int columna) {

        return redisService.obtenerEstadoAsiento(eventoId, fila, columna);
    }

    @GetMapping("/clave/{key}")
    public Object obtenerClave(@PathVariable String key) {
        return redisService.obtenerClave(key);
    }
}

