package fr.vmz.jhipster.petclinic.customer.repository;

import fr.vmz.jhipster.petclinic.customer.domain.PetType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PetType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PetTypeRepository extends JpaRepository<PetType, Long> {}
