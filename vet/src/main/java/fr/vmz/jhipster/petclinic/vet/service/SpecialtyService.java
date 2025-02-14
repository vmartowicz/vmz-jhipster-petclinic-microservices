package fr.vmz.jhipster.petclinic.vet.service;

import fr.vmz.jhipster.petclinic.vet.domain.Specialty;
import fr.vmz.jhipster.petclinic.vet.repository.SpecialtyRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.vet.domain.Specialty}.
 */
@Service
@Transactional
public class SpecialtyService {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialtyService.class);

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    /**
     * Save a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    public Specialty save(Specialty specialty) {
        LOG.debug("Request to save Specialty : {}", specialty);
        return specialtyRepository.save(specialty);
    }

    /**
     * Update a specialty.
     *
     * @param specialty the entity to save.
     * @return the persisted entity.
     */
    public Specialty update(Specialty specialty) {
        LOG.debug("Request to update Specialty : {}", specialty);
        return specialtyRepository.save(specialty);
    }

    /**
     * Partially update a specialty.
     *
     * @param specialty the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Specialty> partialUpdate(Specialty specialty) {
        LOG.debug("Request to partially update Specialty : {}", specialty);

        return specialtyRepository
            .findById(specialty.getId())
            .map(existingSpecialty -> {
                if (specialty.getName() != null) {
                    existingSpecialty.setName(specialty.getName());
                }

                return existingSpecialty;
            })
            .map(specialtyRepository::save);
    }

    /**
     * Get all the specialties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Specialty> findAll(Pageable pageable) {
        LOG.debug("Request to get all Specialties");
        return specialtyRepository.findAll(pageable);
    }

    /**
     * Get one specialty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Specialty> findOne(Long id) {
        LOG.debug("Request to get Specialty : {}", id);
        return specialtyRepository.findById(id);
    }

    /**
     * Delete the specialty by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
