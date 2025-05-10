package fr.vmz.jhipster.petclinic.customer.security.jwt;

import fr.vmz.jhipster.petclinic.customer.config.SecurityConfiguration;
import fr.vmz.jhipster.petclinic.customer.config.SecurityJwtConfiguration;
import fr.vmz.jhipster.petclinic.customer.config.WebConfigurer;
import fr.vmz.jhipster.petclinic.customer.management.SecurityMetersService;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import tech.jhipster.config.JHipsterProperties;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        JHipsterProperties.class,
        WebConfigurer.class,
        SecurityConfiguration.class,
        SecurityJwtConfiguration.class,
        SecurityMetersService.class,
        JwtAuthenticationTestUtils.class,
    }
)
public @interface AuthenticationIntegrationTest {
}
