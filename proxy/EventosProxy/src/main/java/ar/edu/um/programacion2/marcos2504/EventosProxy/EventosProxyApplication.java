package ar.edu.um.programacion2.marcos2504.EventosProxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EventosProxyApplication {
	public static void main(String[] args) {
		SpringApplication.run(EventosProxyApplication.class, args);
	}
}
