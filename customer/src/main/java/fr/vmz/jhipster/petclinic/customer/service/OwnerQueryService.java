package fr.vmz.jhipster.petclinic.customer.service;

import fr.vmz.jhipster.petclinic.customer.domain.*; // for static metamodels
import fr.vmz.jhipster.petclinic.customer.domain.Owner;
import fr.vmz.jhipster.petclinic.customer.repository.OwnerRepository;
import fr.vmz.jhipster.petclinic.customer.service.criteria.OwnerCriteria;
import fr.vmz.jhipster.petclinic.customer.service.dto.OwnerDTO;
import fr.vmz.jhipster.petclinic.customer.service.mapper.OwnerMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Owner} entities in the database.
 * The main input is a {@link OwnerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OwnerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OwnerQueryService extends QueryService<Owner> {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerQueryService.class);

    private final OwnerRepository ownerRepository;

    private final OwnerMapper ownerMapper;

    public OwnerQueryService(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    /**
     * Return a {@link Page} of {@link OwnerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OwnerDTO> findByCriteria(OwnerCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerRepository.findAll(specification, page).map(ownerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OwnerCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Owner> specification = createSpecification(criteria);
        return ownerRepository.count(specification);
    }

    /**
     * Function to convert {@link OwnerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Owner> createSpecification(OwnerCriteria criteria) {
        Specification<Owner> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Owner_.id),
                buildStringSpecification(criteria.getFirstName(), Owner_.firstName),
                buildStringSpecification(criteria.getLastName(), Owner_.lastName),
                buildStringSpecification(criteria.getAddress(), Owner_.address),
                buildStringSpecification(criteria.getCity(), Owner_.city),
                buildStringSpecification(criteria.getTelephone(), Owner_.telephone),
                buildSpecification(criteria.getPetsId(), root -> root.join(Owner_.pets, JoinType.LEFT).get(Pet_.id))
            );
        }
        return specification;
    }
}
