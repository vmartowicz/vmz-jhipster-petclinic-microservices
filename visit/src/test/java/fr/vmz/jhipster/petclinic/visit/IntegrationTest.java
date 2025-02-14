package fr.vmz.jhipster.petclinic.visit;

import fr.vmz.jhipster.petclinic.visit.config.AsyncSyncConfiguration;
import fr.vmz.jhipster.petclinic.visit.config.EmbeddedSQL;
import fr.vmz.jhipster.petclinic.visit.config.JacksonConfiguration;
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
@SpringBootTest(classes = { VisitApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
