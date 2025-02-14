package fr.vmz.jhipster.petclinic.vet.repository;

import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface VetRepositoryWithBagRelationships {
    Optional<Vet> fetchBagRelationships(Optional<Vet> vet);

    List<Vet> fetchBagRelationships(List<Vet> vets);

    Page<Vet> fetchBagRelationships(Page<Vet> vets);
}
