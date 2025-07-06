package fr.vmz.jhipster.petclinic.vet.service.mapper;

import fr.vmz.jhipster.petclinic.vet.domain.Specialty;
import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import fr.vmz.jhipster.petclinic.vet.service.dto.SpecialtyDTO;
import fr.vmz.jhipster.petclinic.vet.service.dto.VetDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Specialty} and its DTO {@link SpecialtyDTO}.
 */
@Mapper(componentModel = "spring")
public interface SpecialtyMapper extends EntityMapper<SpecialtyDTO, Specialty> {
    @Mapping(target = "vets", source = "vets", qualifiedByName = "vetLastNameSet")
    SpecialtyDTO toDto(Specialty s);

    @Mapping(target = "vets", ignore = true)
    @Mapping(target = "removeVets", ignore = true)
    Specialty toEntity(SpecialtyDTO specialtyDTO);

    @Named("vetLastName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastName", source = "lastName")
    VetDTO toDtoVetLastName(Vet vet);

    @Named("vetLastNameSet")
    default Set<VetDTO> toDtoVetLastNameSet(Set<Vet> vet) {
        return vet.stream().map(this::toDtoVetLastName).collect(Collectors.toSet());
    }
}
