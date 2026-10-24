package fr.vmz.jhipster.petclinic.customer;

import fr.vmz.jhipster.petclinic.customer.config.AsyncSyncConfiguration;
import fr.vmz.jhipster.petclinic.customer.config.EmbeddedSQL;
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
@SpringBootTest(
    classes = {
        CustomerApp.class,
        AsyncSyncConfiguration.class,
        fr.vmz.jhipster.petclinic.customer.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
