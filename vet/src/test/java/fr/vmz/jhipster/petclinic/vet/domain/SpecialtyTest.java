package fr.vmz.jhipster.petclinic.vet.domain;

import static fr.vmz.jhipster.petclinic.vet.domain.SpecialtyTestSamples.*;
import static fr.vmz.jhipster.petclinic.vet.domain.VetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.vmz.jhipster.petclinic.vet.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SpecialtyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Specialty.class);
        Specialty specialty1 = getSpecialtySample1();
        Specialty specialty2 = new Specialty();
        assertThat(specialty1).isNotEqualTo(specialty2);

        specialty2.setId(specialty1.getId());
        assertThat(specialty1).isEqualTo(specialty2);

        specialty2 = getSpecialtySample2();
        assertThat(specialty1).isNotEqualTo(specialty2);
    }

    @Test
    void vetsTest() {
        Specialty specialty = getSpecialtyRandomSampleGenerator();
        Vet vetBack = getVetRandomSampleGenerator();

        specialty.addVets(vetBack);
        assertThat(specialty.getVets()).containsOnly(vetBack);
        assertThat(vetBack.getSpecialties()).containsOnly(specialty);

        specialty.removeVets(vetBack);
        assertThat(specialty.getVets()).doesNotContain(vetBack);
        assertThat(vetBack.getSpecialties()).doesNotContain(specialty);

        specialty.vets(new HashSet<>(Set.of(vetBack)));
        assertThat(specialty.getVets()).containsOnly(vetBack);
        assertThat(vetBack.getSpecialties()).containsOnly(specialty);

        specialty.setVets(new HashSet<>());
        assertThat(specialty.getVets()).doesNotContain(vetBack);
        assertThat(vetBack.getSpecialties()).doesNotContain(specialty);
    }
}
