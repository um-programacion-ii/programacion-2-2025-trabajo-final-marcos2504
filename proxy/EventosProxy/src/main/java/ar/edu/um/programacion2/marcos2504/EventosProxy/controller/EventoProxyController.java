package ar.edu.um.programacion2.marcos2504.EventosProxy.controller;

import ar.edu.um.programacion2.marcos2504.EventosProxy.service.CatedraProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy/eventos")
@RequiredArgsConstructor
public class EventoProxyController {

    private final CatedraProxyService service;

    @GetMapping("/resumidos")
    public ResponseEntity<?> getResumen() {
        return ResponseEntity.ok(service.eventosResumidos());
    }

    @GetMapping
    public ResponseEntity<?> getCompletos() {
        return ResponseEntity.ok(service.eventos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEvento(@PathVariable Long id) {
        return ResponseEntity.ok(service.eventoPorId(id));
    }
}
