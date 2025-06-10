package fr.vmz.jhipster.petclinic.visit.repository;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Visit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {

    List<Visit> findByPetIdIn(List<Integer> petIds);

}
