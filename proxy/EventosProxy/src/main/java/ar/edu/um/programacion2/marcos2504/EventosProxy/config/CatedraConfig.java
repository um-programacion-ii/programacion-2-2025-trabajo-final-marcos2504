package ar.edu.um.programacion2.marcos2504.EventosProxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "catedra")
@Data
public class CatedraConfig {
    private String baseUrl;
    private String token;
}
