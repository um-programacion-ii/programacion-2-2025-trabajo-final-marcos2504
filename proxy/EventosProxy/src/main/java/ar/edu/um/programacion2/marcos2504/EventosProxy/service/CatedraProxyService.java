package ar.edu.um.programacion2.marcos2504.EventosProxy.service;

import ar.edu.um.programacion2.marcos2504.EventosProxy.client.CatedraClient;
import ar.edu.um.programacion2.marcos2504.EventosProxy.config.CatedraConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatedraProxyService {

    private final CatedraClient client;
    private final CatedraConfig config;

    private String auth() {
        return "Bearer " + config.getToken();
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
}

