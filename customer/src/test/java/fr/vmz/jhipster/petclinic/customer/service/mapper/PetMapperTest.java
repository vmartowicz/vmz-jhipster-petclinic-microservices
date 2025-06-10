package fr.vmz.jhipster.petclinic.customer.service.mapper;

import static fr.vmz.jhipster.petclinic.customer.domain.PetAsserts.*;
import static fr.vmz.jhipster.petclinic.customer.domain.PetTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PetMapperTest {

    private PetMapper petMapper;

    @BeforeEach
    void setUp() {
        petMapper = new PetMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPetSample1();
        var actual = petMapper.toEntity(petMapper.toDto(expected));
        assertPetAllPropertiesEquals(expected, actual);
    }
}
