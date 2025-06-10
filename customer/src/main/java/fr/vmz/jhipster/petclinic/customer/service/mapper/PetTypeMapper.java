package fr.vmz.jhipster.petclinic.customer.service.mapper;

import fr.vmz.jhipster.petclinic.customer.domain.PetType;
import fr.vmz.jhipster.petclinic.customer.service.dto.PetTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PetType} and its DTO {@link PetTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface PetTypeMapper extends EntityMapper<PetTypeDTO, PetType> {}
