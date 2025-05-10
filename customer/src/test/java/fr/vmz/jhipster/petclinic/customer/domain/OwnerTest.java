package fr.vmz.jhipster.petclinic.customer.domain;

import static fr.vmz.jhipster.petclinic.customer.domain.OwnerTestSamples.*;
import static fr.vmz.jhipster.petclinic.customer.domain.PetTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.vmz.jhipster.petclinic.customer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OwnerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Owner.class);
        Owner owner1 = getOwnerSample1();
        Owner owner2 = new Owner();
        assertThat(owner1).isNotEqualTo(owner2);

        owner2.setId(owner1.getId());
        assertThat(owner1).isEqualTo(owner2);

        owner2 = getOwnerSample2();
        assertThat(owner1).isNotEqualTo(owner2);
    }

    @Test
    void petsTest() {
        Owner owner = getOwnerRandomSampleGenerator();
        Pet petBack = getPetRandomSampleGenerator();

        owner.addPets(petBack);
        assertThat(owner.getPets()).containsOnly(petBack);
        assertThat(petBack.getOwner()).isEqualTo(owner);

        owner.removePets(petBack);
        assertThat(owner.getPets()).doesNotContain(petBack);
        assertThat(petBack.getOwner()).isNull();

        owner.pets(new HashSet<>(Set.of(petBack)));
        assertThat(owner.getPets()).containsOnly(petBack);
        assertThat(petBack.getOwner()).isEqualTo(owner);

        owner.setPets(new HashSet<>());
        assertThat(owner.getPets()).doesNotContain(petBack);
        assertThat(petBack.getOwner()).isNull();
    }
}
