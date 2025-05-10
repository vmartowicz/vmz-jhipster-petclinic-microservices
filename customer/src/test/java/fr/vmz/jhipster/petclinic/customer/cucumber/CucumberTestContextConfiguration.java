package fr.vmz.jhipster.petclinic.customer.cucumber;

import fr.vmz.jhipster.petclinic.customer.IntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
