package fr.vmz.jhipster.petclinic.vet.service;

import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.vmz.jhipster.petclinic.vet.domain.Vet}.
 */
public interface VetService {
    /**
     * Save a vet.
     *
     * @param vet the entity to save.
     * @return the persisted entity.
     */
    Vet save(Vet vet);

    /**
     * Updates a vet.
     *
     * @param vet the entity to update.
     * @return the persisted entity.
     */
    Vet update(Vet vet);

    /**
     * Partially updates a vet.
     *
     * @param vet the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Vet> partialUpdate(Vet vet);

    /**
     * Get all the vets with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Vet> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" vet.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Vet> findOne(Long id);

    /**
     * Delete the "id" vet.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
