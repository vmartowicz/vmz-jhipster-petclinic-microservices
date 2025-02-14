package fr.vmz.jhipster.petclinic.vet;

import fr.vmz.jhipster.petclinic.vet.config.AsyncSyncConfiguration;
import fr.vmz.jhipster.petclinic.vet.config.EmbeddedSQL;
import fr.vmz.jhipster.petclinic.vet.config.JacksonConfiguration;
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
@SpringBootTest(classes = { VetApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
