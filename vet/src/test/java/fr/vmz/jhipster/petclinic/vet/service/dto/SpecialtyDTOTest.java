package fr.vmz.jhipster.petclinic.vet.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.vmz.jhipster.petclinic.vet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpecialtyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpecialtyDTO.class);
        SpecialtyDTO specialtyDTO1 = new SpecialtyDTO();
        specialtyDTO1.setId(1L);
        SpecialtyDTO specialtyDTO2 = new SpecialtyDTO();
        assertThat(specialtyDTO1).isNotEqualTo(specialtyDTO2);
        specialtyDTO2.setId(specialtyDTO1.getId());
        assertThat(specialtyDTO1).isEqualTo(specialtyDTO2);
        specialtyDTO2.setId(2L);
        assertThat(specialtyDTO1).isNotEqualTo(specialtyDTO2);
        specialtyDTO1.setId(null);
        assertThat(specialtyDTO1).isNotEqualTo(specialtyDTO2);
    }
}
