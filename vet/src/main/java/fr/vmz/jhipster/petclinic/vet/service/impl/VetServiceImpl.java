package fr.vmz.jhipster.petclinic.vet.service.impl;

import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import fr.vmz.jhipster.petclinic.vet.repository.VetRepository;
import fr.vmz.jhipster.petclinic.vet.service.VetService;
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
public class VetServiceImpl implements VetService {

    private static final Logger LOG = LoggerFactory.getLogger(VetServiceImpl.class);

    private final VetRepository vetRepository;

    public VetServiceImpl(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    @Override
    public Vet save(Vet vet) {
        LOG.debug("Request to save Vet : {}", vet);
        return vetRepository.save(vet);
    }

    @Override
    public Vet update(Vet vet) {
        LOG.debug("Request to update Vet : {}", vet);
        return vetRepository.save(vet);
    }

    @Override
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

    public Page<Vet> findAllWithEagerRelationships(Pageable pageable) {
        return vetRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vet> findOne(Long id) {
        LOG.debug("Request to get Vet : {}", id);
        return vetRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Vet : {}", id);
        vetRepository.deleteById(id);
    }
}
