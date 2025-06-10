package fr.vmz.jhipster.petclinic.visit.service;

import fr.vmz.jhipster.petclinic.visit.domain.*; // for static metamodels
import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import fr.vmz.jhipster.petclinic.visit.repository.VisitRepository;
import fr.vmz.jhipster.petclinic.visit.service.criteria.VisitCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Visit} entities in the database.
 * The main input is a {@link VisitCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Visit} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VisitQueryService extends QueryService<Visit> {

    private static final Logger LOG = LoggerFactory.getLogger(VisitQueryService.class);

    private final VisitRepository visitRepository;

    public VisitQueryService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    /**
     * Return a {@link Page} of {@link Visit} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Visit> findByCriteria(VisitCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Visit> specification = createSpecification(criteria);
        return visitRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VisitCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Visit> specification = createSpecification(criteria);
        return visitRepository.count(specification);
    }

    /**
     * Function to convert {@link VisitCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Visit> createSpecification(VisitCriteria criteria) {
        Specification<Visit> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Visit_.id),
                buildRangeSpecification(criteria.getVisitDate(), Visit_.visitDate),
                buildStringSpecification(criteria.getDescription(), Visit_.description),
                buildRangeSpecification(criteria.getPetId(), Visit_.petId)
            );
        }
        return specification;
    }
}
