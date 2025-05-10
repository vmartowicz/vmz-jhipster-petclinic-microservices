package fr.vmz.jhipster.petclinic.customer.service;

import fr.vmz.jhipster.petclinic.customer.domain.Pet;
import fr.vmz.jhipster.petclinic.customer.repository.PetRepository;
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
public class PetService {

    private static final Logger LOG = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Save a pet.
     *
     * @param pet the entity to save.
     * @return the persisted entity.
     */
    public Pet save(Pet pet) {
        LOG.debug("Request to save Pet : {}", pet);
        return petRepository.save(pet);
    }

    /**
     * Update a pet.
     *
     * @param pet the entity to save.
     * @return the persisted entity.
     */
    public Pet update(Pet pet) {
        LOG.debug("Request to update Pet : {}", pet);
        return petRepository.save(pet);
    }

    /**
     * Partially update a pet.
     *
     * @param pet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pet> partialUpdate(Pet pet) {
        LOG.debug("Request to partially update Pet : {}", pet);

        return petRepository
            .findById(pet.getId())
            .map(existingPet -> {
                if (pet.getName() != null) {
                    existingPet.setName(pet.getName());
                }
                if (pet.getBirthDate() != null) {
                    existingPet.setBirthDate(pet.getBirthDate());
                }

                return existingPet;
            })
            .map(petRepository::save);
    }

    /**
     * Get all the pets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Pet> findAll(Pageable pageable) {
        LOG.debug("Request to get all Pets");
        return petRepository.findAll(pageable);
    }

    /**
     * Get all the pets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Pet> findAllWithEagerRelationships(Pageable pageable) {
        return petRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one pet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pet> findOne(Long id) {
        LOG.debug("Request to get Pet : {}", id);
        return petRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the pet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Pet : {}", id);
        petRepository.deleteById(id);
    }
}
