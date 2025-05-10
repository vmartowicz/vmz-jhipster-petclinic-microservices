package fr.vmz.jhipster.petclinic.visit.security.jwt;

import fr.vmz.jhipster.petclinic.visit.config.SecurityConfiguration;
import fr.vmz.jhipster.petclinic.visit.config.SecurityJwtConfiguration;
import fr.vmz.jhipster.petclinic.visit.config.WebConfigurer;
import fr.vmz.jhipster.petclinic.visit.management.SecurityMetersService;
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
