package fr.vmz.jhipster.petclinic.vet.service.impl;

import fr.vmz.jhipster.petclinic.vet.domain.Specialty;
import fr.vmz.jhipster.petclinic.vet.repository.SpecialtyRepository;
import fr.vmz.jhipster.petclinic.vet.service.SpecialtyService;
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
public class SpecialtyServiceImpl implements SpecialtyService {

    private static final Logger LOG = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public Specialty save(Specialty specialty) {
        LOG.debug("Request to save Specialty : {}", specialty);
        return specialtyRepository.save(specialty);
    }

    @Override
    public Specialty update(Specialty specialty) {
        LOG.debug("Request to update Specialty : {}", specialty);
        return specialtyRepository.save(specialty);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public Page<Specialty> findAll(Pageable pageable) {
        LOG.debug("Request to get all Specialties");
        return specialtyRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Specialty> findOne(Long id) {
        LOG.debug("Request to get Specialty : {}", id);
        return specialtyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
    }
}
