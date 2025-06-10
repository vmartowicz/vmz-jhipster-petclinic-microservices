package fr.vmz.jhipster.petclinic.customer.service;

import fr.vmz.jhipster.petclinic.customer.service.dto.PetTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.vmz.jhipster.petclinic.customer.domain.PetType}.
 */
public interface PetTypeService {
    /**
     * Save a petType.
     *
     * @param petTypeDTO the entity to save.
     * @return the persisted entity.
     */
    PetTypeDTO save(PetTypeDTO petTypeDTO);

    /**
     * Updates a petType.
     *
     * @param petTypeDTO the entity to update.
     * @return the persisted entity.
     */
    PetTypeDTO update(PetTypeDTO petTypeDTO);

    /**
     * Partially updates a petType.
     *
     * @param petTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PetTypeDTO> partialUpdate(PetTypeDTO petTypeDTO);

    /**
     * Get all the petTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PetTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" petType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PetTypeDTO> findOne(Long id);

    /**
     * Delete the "id" petType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
