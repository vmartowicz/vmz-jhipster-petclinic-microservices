package fr.vmz.jhipster.petclinic.visit.repository;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Visit entity.
 */
@SuppressWarnings("unused")
@Repository
@Primary
public interface VisitRepositoryExtended extends VisitRepository {

    Page<Visit> findByPetIdIn(List<Integer> petIds, Pageable pageable);

}
