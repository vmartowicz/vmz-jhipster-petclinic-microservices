package fr.vmz.jhipster.petclinic.customer.service.mapper;

import static fr.vmz.jhipster.petclinic.customer.domain.PetTypeAsserts.*;
import static fr.vmz.jhipster.petclinic.customer.domain.PetTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PetTypeMapperTest {

    private PetTypeMapper petTypeMapper;

    @BeforeEach
    void setUp() {
        petTypeMapper = new PetTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPetTypeSample1();
        var actual = petTypeMapper.toEntity(petTypeMapper.toDto(expected));
        assertPetTypeAllPropertiesEquals(expected, actual);
    }
}
