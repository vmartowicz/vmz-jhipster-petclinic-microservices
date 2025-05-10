package fr.vmz.jhipster.petclinic.customer.service;

import fr.vmz.jhipster.petclinic.customer.domain.PetType;
import fr.vmz.jhipster.petclinic.customer.repository.PetTypeRepository;
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
public class PetTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(PetTypeService.class);

    private final PetTypeRepository petTypeRepository;

    public PetTypeService(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    /**
     * Save a petType.
     *
     * @param petType the entity to save.
     * @return the persisted entity.
     */
    public PetType save(PetType petType) {
        LOG.debug("Request to save PetType : {}", petType);
        return petTypeRepository.save(petType);
    }

    /**
     * Update a petType.
     *
     * @param petType the entity to save.
     * @return the persisted entity.
     */
    public PetType update(PetType petType) {
        LOG.debug("Request to update PetType : {}", petType);
        return petTypeRepository.save(petType);
    }

    /**
     * Partially update a petType.
     *
     * @param petType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PetType> partialUpdate(PetType petType) {
        LOG.debug("Request to partially update PetType : {}", petType);

        return petTypeRepository
            .findById(petType.getId())
            .map(existingPetType -> {
                if (petType.getName() != null) {
                    existingPetType.setName(petType.getName());
                }

                return existingPetType;
            })
            .map(petTypeRepository::save);
    }

    /**
     * Get all the petTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PetType> findAll(Pageable pageable) {
        LOG.debug("Request to get all PetTypes");
        return petTypeRepository.findAll(pageable);
    }

    /**
     * Get one petType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PetType> findOne(Long id) {
        LOG.debug("Request to get PetType : {}", id);
        return petTypeRepository.findById(id);
    }

    /**
     * Delete the petType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PetType : {}", id);
        petTypeRepository.deleteById(id);
    }
}
