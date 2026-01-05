package fr.vmz.jhipster.petclinic.visit.service;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VisitServiceExtended extends VisitService {

    Page<Visit> findByPetIdIn(List<Integer> petIds, Pageable pageable);

}
