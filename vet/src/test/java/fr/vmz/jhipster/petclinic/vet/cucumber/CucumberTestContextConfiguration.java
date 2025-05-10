package fr.vmz.jhipster.petclinic.vet.cucumber;

import fr.vmz.jhipster.petclinic.vet.IntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
