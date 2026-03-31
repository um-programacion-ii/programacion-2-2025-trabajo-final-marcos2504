package ar.edu.um.programacion2.marcos2504.EventosProxy.controller;

import ar.edu.um.programacion2.marcos2504.EventosProxy.service.CatedraProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/bloquear-asientos")
    public ResponseEntity<?> bloquearAsientos(@RequestBody Object request) {
        return ResponseEntity.ok(service.bloquearAsientos(request));
    }

    @PostMapping("/realizar-venta")
    public ResponseEntity<?> realizarVenta(@RequestBody Object request) {
        return ResponseEntity.ok(service.realizarVenta(request));
    }

    @GetMapping("/ventas")
    public ResponseEntity<?> listarVentas() {
        return ResponseEntity.ok(service.listarVentas());
    }

    @GetMapping("/ventas/{id}")
    public ResponseEntity<?> listarVenta(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarVenta(id));
    }
}