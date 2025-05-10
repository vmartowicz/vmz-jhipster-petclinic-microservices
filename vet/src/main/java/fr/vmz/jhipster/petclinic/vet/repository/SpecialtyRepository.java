package fr.vmz.jhipster.petclinic.vet.repository;

import fr.vmz.jhipster.petclinic.vet.domain.Specialty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Specialty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {}
