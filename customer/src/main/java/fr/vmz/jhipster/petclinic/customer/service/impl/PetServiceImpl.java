package fr.vmz.jhipster.petclinic.customer.service.impl;

import fr.vmz.jhipster.petclinic.customer.domain.Pet;
import fr.vmz.jhipster.petclinic.customer.repository.PetRepository;
import fr.vmz.jhipster.petclinic.customer.service.PetService;
import fr.vmz.jhipster.petclinic.customer.service.dto.PetDTO;
import fr.vmz.jhipster.petclinic.customer.service.mapper.PetMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.customer.domain.Pet}.
 */
@Service
@Transactional
public class PetServiceImpl implements PetService {

    private static final Logger LOG = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetRepository petRepository;

    private final PetMapper petMapper;

    public PetServiceImpl(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    @Override
    public PetDTO save(PetDTO petDTO) {
        LOG.debug("Request to save Pet : {}", petDTO);
        Pet pet = petMapper.toEntity(petDTO);
        pet = petRepository.save(pet);
        return petMapper.toDto(pet);
    }

    @Override
    public PetDTO update(PetDTO petDTO) {
        LOG.debug("Request to update Pet : {}", petDTO);
        Pet pet = petMapper.toEntity(petDTO);
        pet = petRepository.save(pet);
        return petMapper.toDto(pet);
    }

    @Override
    public Optional<PetDTO> partialUpdate(PetDTO petDTO) {
        LOG.debug("Request to partially update Pet : {}", petDTO);

        return petRepository
            .findById(petDTO.getId())
            .map(existingPet -> {
                petMapper.partialUpdate(existingPet, petDTO);

                return existingPet;
            })
            .map(petRepository::save)
            .map(petMapper::toDto);
    }

    public Page<PetDTO> findAllWithEagerRelationships(Pageable pageable) {
        return petRepository.findAllWithEagerRelationships(pageable).map(petMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetDTO> findOne(Long id) {
        LOG.debug("Request to get Pet : {}", id);
        return petRepository.findOneWithEagerRelationships(id).map(petMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Pet : {}", id);
        petRepository.deleteById(id);
    }
}
