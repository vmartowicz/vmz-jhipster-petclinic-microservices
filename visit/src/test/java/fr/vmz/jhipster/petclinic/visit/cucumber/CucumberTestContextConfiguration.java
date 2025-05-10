package fr.vmz.jhipster.petclinic.visit.cucumber;

import fr.vmz.jhipster.petclinic.visit.IntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
