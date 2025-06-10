package fr.vmz.jhipster.petclinic.customer.service.impl;

import fr.vmz.jhipster.petclinic.customer.domain.PetType;
import fr.vmz.jhipster.petclinic.customer.repository.PetTypeRepository;
import fr.vmz.jhipster.petclinic.customer.service.PetTypeService;
import fr.vmz.jhipster.petclinic.customer.service.dto.PetTypeDTO;
import fr.vmz.jhipster.petclinic.customer.service.mapper.PetTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.customer.domain.PetType}.
 */
@Service
@Transactional
public class PetTypeServiceImpl implements PetTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PetTypeServiceImpl.class);

    private final PetTypeRepository petTypeRepository;

    private final PetTypeMapper petTypeMapper;

    public PetTypeServiceImpl(PetTypeRepository petTypeRepository, PetTypeMapper petTypeMapper) {
        this.petTypeRepository = petTypeRepository;
        this.petTypeMapper = petTypeMapper;
    }

    @Override
    public PetTypeDTO save(PetTypeDTO petTypeDTO) {
        LOG.debug("Request to save PetType : {}", petTypeDTO);
        PetType petType = petTypeMapper.toEntity(petTypeDTO);
        petType = petTypeRepository.save(petType);
        return petTypeMapper.toDto(petType);
    }

    @Override
    public PetTypeDTO update(PetTypeDTO petTypeDTO) {
        LOG.debug("Request to update PetType : {}", petTypeDTO);
        PetType petType = petTypeMapper.toEntity(petTypeDTO);
        petType = petTypeRepository.save(petType);
        return petTypeMapper.toDto(petType);
    }

    @Override
    public Optional<PetTypeDTO> partialUpdate(PetTypeDTO petTypeDTO) {
        LOG.debug("Request to partially update PetType : {}", petTypeDTO);

        return petTypeRepository
            .findById(petTypeDTO.getId())
            .map(existingPetType -> {
                petTypeMapper.partialUpdate(existingPetType, petTypeDTO);

                return existingPetType;
            })
            .map(petTypeRepository::save)
            .map(petTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PetTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PetTypes");
        return petTypeRepository.findAll(pageable).map(petTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetTypeDTO> findOne(Long id) {
        LOG.debug("Request to get PetType : {}", id);
        return petTypeRepository.findById(id).map(petTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PetType : {}", id);
        petTypeRepository.deleteById(id);
    }
}
