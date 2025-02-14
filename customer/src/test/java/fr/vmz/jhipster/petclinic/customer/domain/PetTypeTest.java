package fr.vmz.jhipster.petclinic.customer.domain;

import static fr.vmz.jhipster.petclinic.customer.domain.PetTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.vmz.jhipster.petclinic.customer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PetTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PetType.class);
        PetType petType1 = getPetTypeSample1();
        PetType petType2 = new PetType();
        assertThat(petType1).isNotEqualTo(petType2);

        petType2.setId(petType1.getId());
        assertThat(petType1).isEqualTo(petType2);

        petType2 = getPetTypeSample2();
        assertThat(petType1).isNotEqualTo(petType2);
    }
}
