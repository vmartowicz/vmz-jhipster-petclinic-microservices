package fr.vmz.jhipster.petclinic.visit.service;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.vmz.jhipster.petclinic.visit.domain.Visit}.
 */
public interface VisitService {
    /**
     * Save a visit.
     *
     * @param visit the entity to save.
     * @return the persisted entity.
     */
    Visit save(Visit visit);

    /**
     * Updates a visit.
     *
     * @param visit the entity to update.
     * @return the persisted entity.
     */
    Visit update(Visit visit);

    /**
     * Partially updates a visit.
     *
     * @param visit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Visit> partialUpdate(Visit visit);

    /**
     * Get the "id" visit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Visit> findOne(Long id);

    /**
     * Delete the "id" visit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
