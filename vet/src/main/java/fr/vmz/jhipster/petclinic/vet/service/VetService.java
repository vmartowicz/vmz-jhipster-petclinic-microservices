package fr.vmz.jhipster.petclinic.vet.service;

import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import fr.vmz.jhipster.petclinic.vet.repository.VetRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.vet.domain.Vet}.
 */
@Service
@Transactional
public class VetService {

    private static final Logger LOG = LoggerFactory.getLogger(VetService.class);

    private final VetRepository vetRepository;

    public VetService(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    /**
     * Save a vet.
     *
     * @param vet the entity to save.
     * @return the persisted entity.
     */
    public Vet save(Vet vet) {
        LOG.debug("Request to save Vet : {}", vet);
        return vetRepository.save(vet);
    }

    /**
     * Update a vet.
     *
     * @param vet the entity to save.
     * @return the persisted entity.
     */
    public Vet update(Vet vet) {
        LOG.debug("Request to update Vet : {}", vet);
        return vetRepository.save(vet);
    }

    /**
     * Partially update a vet.
     *
     * @param vet the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Vet> partialUpdate(Vet vet) {
        LOG.debug("Request to partially update Vet : {}", vet);

        return vetRepository
            .findById(vet.getId())
            .map(existingVet -> {
                if (vet.getFirstName() != null) {
                    existingVet.setFirstName(vet.getFirstName());
                }
                if (vet.getLastName() != null) {
                    existingVet.setLastName(vet.getLastName());
                }

                return existingVet;
            })
            .map(vetRepository::save);
    }

    /**
     * Get all the vets.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Vet> findAll(Pageable pageable) {
        LOG.debug("Request to get all Vets");
        return vetRepository.findAll(pageable);
    }

    /**
     * Get all the vets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Vet> findAllWithEagerRelationships(Pageable pageable) {
        return vetRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one vet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Vet> findOne(Long id) {
        LOG.debug("Request to get Vet : {}", id);
        return vetRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the vet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Vet : {}", id);
        vetRepository.deleteById(id);
    }
}
