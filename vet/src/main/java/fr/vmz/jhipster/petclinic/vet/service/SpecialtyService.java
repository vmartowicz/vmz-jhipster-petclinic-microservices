package fr.vmz.jhipster.petclinic.vet.service;

import fr.vmz.jhipster.petclinic.vet.domain.Specialty;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.vmz.jhipster.petclinic.vet.domain.Specialty}.
 */
public interface SpecialtyService {
    /**
     * Save a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    Specialty save(Specialty specialty);

    /**
     * Updates a specialty.
     *
     * @param specialty the entity to update.
     * @return the persisted entity.
     */
    Specialty update(Specialty specialty);

    /**
     * Partially updates a specialty.
     *
     * @param specialty the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Specialty> partialUpdate(Specialty specialty);

    /**
     * Get all the specialties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Specialty> findAll(Pageable pageable);

    /**
     * Get the "id" specialty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Specialty> findOne(Long id);

    /**
     * Delete the "id" specialty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
