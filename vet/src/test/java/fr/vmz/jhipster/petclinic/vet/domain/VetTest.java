package fr.vmz.jhipster.petclinic.vet.domain;

import static fr.vmz.jhipster.petclinic.vet.domain.SpecialtyTestSamples.*;
import static fr.vmz.jhipster.petclinic.vet.domain.VetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.vmz.jhipster.petclinic.vet.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class VetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vet.class);
        Vet vet1 = getVetSample1();
        Vet vet2 = new Vet();
        assertThat(vet1).isNotEqualTo(vet2);

        vet2.setId(vet1.getId());
        assertThat(vet1).isEqualTo(vet2);

        vet2 = getVetSample2();
        assertThat(vet1).isNotEqualTo(vet2);
    }

    @Test
    void specialtiesTest() {
        Vet vet = getVetRandomSampleGenerator();
        Specialty specialtyBack = getSpecialtyRandomSampleGenerator();

        vet.addSpecialties(specialtyBack);
        assertThat(vet.getSpecialties()).containsOnly(specialtyBack);

        vet.removeSpecialties(specialtyBack);
        assertThat(vet.getSpecialties()).doesNotContain(specialtyBack);

        vet.specialties(new HashSet<>(Set.of(specialtyBack)));
        assertThat(vet.getSpecialties()).containsOnly(specialtyBack);

        vet.setSpecialties(new HashSet<>());
        assertThat(vet.getSpecialties()).doesNotContain(specialtyBack);
    }
}
