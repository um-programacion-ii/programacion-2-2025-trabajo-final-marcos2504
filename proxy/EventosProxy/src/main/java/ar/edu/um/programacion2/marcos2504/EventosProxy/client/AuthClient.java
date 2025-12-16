package ar.edu.um.programacion2.marcos2504.EventosProxy.client;

import ar.edu.um.programacion2.marcos2504.EventosProxy.dto.LoginRequest;
import ar.edu.um.programacion2.marcos2504.EventosProxy.dto.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "authClient",
        url = "${catedra.auth-url}"
)
public interface AuthClient {

    @PostMapping("/authenticate")
    LoginResponse login(@RequestBody LoginRequest request);
}
