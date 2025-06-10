package fr.vmz.jhipster.petclinic.vet.service;

import fr.vmz.jhipster.petclinic.vet.service.dto.SpecialtyDTO;
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
     * @param specialtyDTO the entity to save.
     * @return the persisted entity.
     */
    SpecialtyDTO save(SpecialtyDTO specialtyDTO);

    /**
     * Updates a specialty.
     *
     * @param specialtyDTO the entity to update.
     * @return the persisted entity.
     */
    SpecialtyDTO update(SpecialtyDTO specialtyDTO);

    /**
     * Partially updates a specialty.
     *
     * @param specialtyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SpecialtyDTO> partialUpdate(SpecialtyDTO specialtyDTO);

    /**
     * Get all the specialties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SpecialtyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" specialty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SpecialtyDTO> findOne(Long id);

    /**
     * Delete the "id" specialty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
