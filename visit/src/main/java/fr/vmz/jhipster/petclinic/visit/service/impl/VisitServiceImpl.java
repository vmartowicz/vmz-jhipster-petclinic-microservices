package fr.vmz.jhipster.petclinic.visit.service.impl;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import fr.vmz.jhipster.petclinic.visit.repository.VisitRepository;
import fr.vmz.jhipster.petclinic.visit.service.VisitService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.vmz.jhipster.petclinic.visit.domain.Visit}.
 */
@Service
@Transactional
public class VisitServiceImpl implements VisitService {

    private static final Logger LOG = LoggerFactory.getLogger(VisitServiceImpl.class);

    private final VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    public Visit save(Visit visit) {
        LOG.debug("Request to save Visit : {}", visit);
        return visitRepository.save(visit);
    }

    @Override
    public Visit update(Visit visit) {
        LOG.debug("Request to update Visit : {}", visit);
        return visitRepository.save(visit);
    }

    @Override
    public Optional<Visit> partialUpdate(Visit visit) {
        LOG.debug("Request to partially update Visit : {}", visit);

        return visitRepository
            .findById(visit.getId())
            .map(existingVisit -> {
                if (visit.getVisitDate() != null) {
                    existingVisit.setVisitDate(visit.getVisitDate());
                }
                if (visit.getDescription() != null) {
                    existingVisit.setDescription(visit.getDescription());
                }
                if (visit.getPetId() != null) {
                    existingVisit.setPetId(visit.getPetId());
                }

                return existingVisit;
            })
            .map(visitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Visit> findOne(Long id) {
        LOG.debug("Request to get Visit : {}", id);
        return visitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Visit : {}", id);
        visitRepository.deleteById(id);
    }
}
