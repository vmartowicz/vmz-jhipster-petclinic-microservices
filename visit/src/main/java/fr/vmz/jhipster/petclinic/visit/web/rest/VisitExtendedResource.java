package fr.vmz.jhipster.petclinic.visit.web.rest;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import fr.vmz.jhipster.petclinic.visit.service.VisitServiceExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

/**
 * REST controller for managing {@link Visit}.
 */
@RestController
@RequestMapping("/api/v2/visits")
public class VisitExtendedResource {

    private static final Logger LOG = LoggerFactory.getLogger(VisitExtendedResource.class);

    private final VisitServiceExtended visitService;

    public VisitExtendedResource(VisitServiceExtended visitService) {
        this.visitService = visitService;
    }


    @GetMapping("")
    public ResponseEntity<List<Visit>> getVisitsByPetId(@RequestParam List<Integer> petIds, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get Visits by pet IDs: {}", petIds);

        Page<Visit> page = visitService.findByPetIdIn(petIds, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok()
            .headers(headers)
            .body(page.getContent());
    }


}
