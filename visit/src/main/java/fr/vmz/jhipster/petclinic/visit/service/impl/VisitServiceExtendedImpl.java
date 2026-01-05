package fr.vmz.jhipster.petclinic.visit.service.impl;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import fr.vmz.jhipster.petclinic.visit.repository.VisitRepository;
import fr.vmz.jhipster.petclinic.visit.repository.VisitRepositoryExtended;
import fr.vmz.jhipster.petclinic.visit.service.VisitServiceExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing {@link Visit}.
 */
@Service
@Transactional
@Primary
public class VisitServiceExtendedImpl extends VisitServiceImpl implements VisitServiceExtended {

    private static final Logger LOG = LoggerFactory.getLogger(VisitServiceExtendedImpl.class);

    private final VisitRepositoryExtended visitExtendedRepository;

    public VisitServiceExtendedImpl(
        VisitRepository visitRepository,
        VisitRepositoryExtended visitExtendedRepository
    ) {
        super(visitRepository);
        this.visitExtendedRepository = visitExtendedRepository;
    }

    @Override
    public Page<Visit> findByPetIdIn(List<Integer> petIds, Pageable pageable) {
        LOG.debug("Request to find pets by Ids");
        return visitExtendedRepository.findByPetIdIn(petIds, pageable);
    }
}
