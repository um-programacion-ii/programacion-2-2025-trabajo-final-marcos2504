package ar.edu.um.programacion2.marcos2504.EventosProxy.kafka;

import ar.edu.um.programacion2.marcos2504.EventosProxy.client.BackendClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Consumer de Kafka que escucha cambios en eventos desde la cátedra
 * y notifica al backend para sincronizar
 */
@Component
public class EventoKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventoKafkaConsumer.class);

    private final BackendClient backendClient;

    public EventoKafkaConsumer(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    /**
     * Escucha mensajes del topic de eventos de la cátedra
     */
    @KafkaListener(topics = "${eventos.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEventoMessage(String message) {
        log.info("📨 Mensaje recibido de Kafka: {}", message);

        try {
            // Notificar al backend para que sincronice usando Feign
            log.info(" Notificando al backend vía Feign...");

            Map<String, Object> response = backendClient.triggerEventoSync();

            log.info("✅ Backend notificado exitosamente. Respuesta: {}", response);

        } catch (Exception e) {
            log.error(" Error notificando al backend", e);
        }
    }
}