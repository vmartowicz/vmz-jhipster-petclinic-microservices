package fr.vmz.jhipster.petclinic.visit.cucumber;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("fr.vmz.jhipster.petclinic.visit.cucumber")
class CucumberTest {}
