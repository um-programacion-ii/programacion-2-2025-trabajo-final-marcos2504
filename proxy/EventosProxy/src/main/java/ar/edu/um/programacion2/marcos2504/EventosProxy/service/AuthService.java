package ar.edu.um.programacion2.marcos2504.EventosProxy.service;

import ar.edu.um.programacion2.marcos2504.EventosProxy.client.AuthClient;
import ar.edu.um.programacion2.marcos2504.EventosProxy.config.CatedraConfig;
import ar.edu.um.programacion2.marcos2504.EventosProxy.dto.LoginRequest;
import ar.edu.um.programacion2.marcos2504.EventosProxy.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthClient authClient;
    private final CatedraConfig config;
    private String cachedToken;

    public String getToken() {
        // Si ya tenemos un, lo usamos
        if (cachedToken != null && !cachedToken.isEmpty()) {
            return cachedToken;
        }

        // No hay token , hacemos login automático
        return refreshToken();
    }

    public String refreshToken() {
        try {
            log.info("Obteniendo nuevo token de autenticación...");
            LoginRequest request = new LoginRequest();
            request.setUsername(config.getUsername());
            request.setPassword(config.getPassword());
            request.setRememberMe(false);

            LoginResponse response = authClient.login(request);
            cachedToken = response.getIdToken();
            log.info("Token obtenido exitosamente");
            return cachedToken;
        } catch (Exception e) {
            log.error("Error al obtener token: {}", e.getMessage());
            throw new RuntimeException("No se pudo obtener el token de autenticación", e);
        }
    }

    public void clearToken() {
        cachedToken = null;
    }
}
