package ar.edu.um.programacion2.marcosibarra;

import ar.edu.um.programacion2.marcosibarra.config.AsyncSyncConfiguration;
import ar.edu.um.programacion2.marcosibarra.config.EmbeddedSQL;
import ar.edu.um.programacion2.marcosibarra.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { EventosApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
