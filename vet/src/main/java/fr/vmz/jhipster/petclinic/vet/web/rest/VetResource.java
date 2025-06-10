package fr.vmz.jhipster.petclinic.vet.web.rest;

import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import fr.vmz.jhipster.petclinic.vet.repository.VetRepository;
import fr.vmz.jhipster.petclinic.vet.service.VetQueryService;
import fr.vmz.jhipster.petclinic.vet.service.VetService;
import fr.vmz.jhipster.petclinic.vet.service.criteria.VetCriteria;
import fr.vmz.jhipster.petclinic.vet.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.vmz.jhipster.petclinic.vet.domain.Vet}.
 */
@RestController
@RequestMapping("/api/vets")
public class VetResource {

    private static final Logger LOG = LoggerFactory.getLogger(VetResource.class);

    private static final String ENTITY_NAME = "vetVet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VetService vetService;

    private final VetRepository vetRepository;

    private final VetQueryService vetQueryService;

    public VetResource(VetService vetService, VetRepository vetRepository, VetQueryService vetQueryService) {
        this.vetService = vetService;
        this.vetRepository = vetRepository;
        this.vetQueryService = vetQueryService;
    }

    /**
     * {@code POST  /vets} : Create a new vet.
     *
     * @param vet the vet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vet, or with status {@code 400 (Bad Request)} if the vet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Vet> createVet(@Valid @RequestBody Vet vet) throws URISyntaxException {
        LOG.debug("REST request to save Vet : {}", vet);
        if (vet.getId() != null) {
            throw new BadRequestAlertException("A new vet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        vet = vetService.save(vet);
        return ResponseEntity.created(new URI("/api/vets/" + vet.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, vet.getId().toString()))
            .body(vet);
    }

    /**
     * {@code PUT  /vets/:id} : Updates an existing vet.
     *
     * @param id the id of the vet to save.
     * @param vet the vet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vet,
     * or with status {@code 400 (Bad Request)} if the vet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vet> updateVet(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Vet vet)
        throws URISyntaxException {
        LOG.debug("REST request to update Vet : {}, {}", id, vet);
        if (vet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        vet = vetService.update(vet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vet.getId().toString()))
            .body(vet);
    }

    /**
     * {@code PATCH  /vets/:id} : Partial updates given fields of an existing vet, field will ignore if it is null
     *
     * @param id the id of the vet to save.
     * @param vet the vet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vet,
     * or with status {@code 400 (Bad Request)} if the vet is not valid,
     * or with status {@code 404 (Not Found)} if the vet is not found,
     * or with status {@code 500 (Internal Server Error)} if the vet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vet> partialUpdateVet(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Vet vet)
        throws URISyntaxException {
        LOG.debug("REST request to partial update Vet partially : {}, {}", id, vet);
        if (vet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vet> result = vetService.partialUpdate(vet);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vet.getId().toString())
        );
    }

    /**
     * {@code GET  /vets} : get all the vets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Vet>> getAllVets(VetCriteria criteria, @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get Vets by criteria: {}", criteria);

        Page<Vet> page = vetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vets/count} : count all the vets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countVets(VetCriteria criteria) {
        LOG.debug("REST request to count Vets by criteria: {}", criteria);
        return ResponseEntity.ok().body(vetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vets/:id} : get the "id" vet.
     *
     * @param id the id of the vet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vet> getVet(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Vet : {}", id);
        Optional<Vet> vet = vetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vet);
    }

    /**
     * {@code DELETE  /vets/:id} : delete the "id" vet.
     *
     * @param id the id of the vet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVet(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Vet : {}", id);
        vetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
