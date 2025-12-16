package ar.edu.um.programacion2.marcos2504.EventosProxy.service;

import ar.edu.um.programacion2.marcos2504.EventosProxy.client.CatedraClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatedraProxyService {

    private final CatedraClient client;
    private final AuthService authService;

    private String auth() {
        return "Bearer " + authService.getToken();
    }

    public List<Object> eventosResumidos() {
        return client.getEventosResumidos(auth());
    }

    public List<Object> eventos() {
        return client.getEventos(auth());
    }

    public Object eventoPorId(Long id) {
        return client.getEvento(id, auth());
    }

    public Object bloquearAsientos(Object request) {
        return client.bloquearAsientos(request, auth());
    }

    public Object realizarVenta(Object request) {
        return client.realizarVenta(request, auth());
    }

    public List<Object> listarVentas() {
        return client.listarVentas(auth());
    }

    public Object listarVenta(Long id) {
        return client.listarVenta(id, auth());
    }
}

