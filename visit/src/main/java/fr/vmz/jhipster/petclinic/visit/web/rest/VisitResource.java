package fr.vmz.jhipster.petclinic.visit.web.rest;

import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import fr.vmz.jhipster.petclinic.visit.repository.VisitRepository;
import fr.vmz.jhipster.petclinic.visit.service.VisitQueryService;
import fr.vmz.jhipster.petclinic.visit.service.VisitService;
import fr.vmz.jhipster.petclinic.visit.service.criteria.VisitCriteria;
import fr.vmz.jhipster.petclinic.visit.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.vmz.jhipster.petclinic.visit.domain.Visit}.
 */
@RestController
@RequestMapping("/api/visits")
public class VisitResource {

    private static final Logger LOG = LoggerFactory.getLogger(VisitResource.class);

    private static final String ENTITY_NAME = "visitVisit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VisitService visitService;

    private final VisitRepository visitRepository;

    private final VisitQueryService visitQueryService;

    public VisitResource(VisitService visitService, VisitRepository visitRepository, VisitQueryService visitQueryService) {
        this.visitService = visitService;
        this.visitRepository = visitRepository;
        this.visitQueryService = visitQueryService;
    }

    /**
     * {@code POST  /visits} : Create a new visit.
     *
     * @param visit the visit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new visit, or with status {@code 400 (Bad Request)} if the visit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Visit> createVisit(@Valid @RequestBody Visit visit) throws URISyntaxException {
        LOG.debug("REST request to save Visit : {}", visit);
        if (visit.getId() != null) {
            throw new BadRequestAlertException("A new visit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        visit = visitService.save(visit);
        return ResponseEntity.created(new URI("/api/visits/" + visit.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, visit.getId().toString()))
            .body(visit);
    }

    /**
     * {@code PUT  /visits/:id} : Updates an existing visit.
     *
     * @param id the id of the visit to save.
     * @param visit the visit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visit,
     * or with status {@code 400 (Bad Request)} if the visit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the visit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Visit> updateVisit(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Visit visit)
        throws URISyntaxException {
        LOG.debug("REST request to update Visit : {}, {}", id, visit);
        if (visit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        visit = visitService.update(visit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visit.getId().toString()))
            .body(visit);
    }

    /**
     * {@code PATCH  /visits/:id} : Partial updates given fields of an existing visit, field will ignore if it is null
     *
     * @param id the id of the visit to save.
     * @param visit the visit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated visit,
     * or with status {@code 400 (Bad Request)} if the visit is not valid,
     * or with status {@code 404 (Not Found)} if the visit is not found,
     * or with status {@code 500 (Internal Server Error)} if the visit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Visit> partialUpdateVisit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Visit visit
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Visit partially : {}, {}", id, visit);
        if (visit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, visit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!visitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Visit> result = visitService.partialUpdate(visit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, visit.getId().toString())
        );
    }

    /**
     * {@code GET  /visits} : get all the visits.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of visits in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Visit>> getAllVisits(
        VisitCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Visits by criteria: {}", criteria);

        Page<Visit> page = visitQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /visits/count} : count all the visits.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVisits(VisitCriteria criteria) {
        LOG.debug("REST request to count Visits by criteria: {}", criteria);
        return ResponseEntity.ok().body(visitQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /visits/:id} : get the "id" visit.
     *
     * @param id the id of the visit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the visit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Visit> getVisit(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Visit : {}", id);
        Optional<Visit> visit = visitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(visit);
    }

    /**
     * {@code DELETE  /visits/:id} : delete the "id" visit.
     *
     * @param id the id of the visit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Visit : {}", id);
        visitService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
