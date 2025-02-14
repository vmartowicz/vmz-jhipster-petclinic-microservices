package fr.vmz.jhipster.petclinic.customer.web.rest;

import fr.vmz.jhipster.petclinic.customer.domain.PetType;
import fr.vmz.jhipster.petclinic.customer.repository.PetTypeRepository;
import fr.vmz.jhipster.petclinic.customer.service.PetTypeService;
import fr.vmz.jhipster.petclinic.customer.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.vmz.jhipster.petclinic.customer.domain.PetType}.
 */
@RestController
@RequestMapping("/api/pet-types")
public class PetTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(PetTypeResource.class);

    private static final String ENTITY_NAME = "customerPetType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PetTypeService petTypeService;

    private final PetTypeRepository petTypeRepository;

    public PetTypeResource(PetTypeService petTypeService, PetTypeRepository petTypeRepository) {
        this.petTypeService = petTypeService;
        this.petTypeRepository = petTypeRepository;
    }

    /**
     * {@code POST  /pet-types} : Create a new petType.
     *
     * @param petType the petType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new petType, or with status {@code 400 (Bad Request)} if the petType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PetType> createPetType(@Valid @RequestBody PetType petType) throws URISyntaxException {
        LOG.debug("REST request to save PetType : {}", petType);
        if (petType.getId() != null) {
            throw new BadRequestAlertException("A new petType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        petType = petTypeService.save(petType);
        return ResponseEntity.created(new URI("/api/pet-types/" + petType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, petType.getId().toString()))
            .body(petType);
    }

    /**
     * {@code PUT  /pet-types/:id} : Updates an existing petType.
     *
     * @param id the id of the petType to save.
     * @param petType the petType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petType,
     * or with status {@code 400 (Bad Request)} if the petType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the petType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PetType> updatePetType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PetType petType
    ) throws URISyntaxException {
        LOG.debug("REST request to update PetType : {}, {}", id, petType);
        if (petType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        petType = petTypeService.update(petType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petType.getId().toString()))
            .body(petType);
    }

    /**
     * {@code PATCH  /pet-types/:id} : Partial updates given fields of an existing petType, field will ignore if it is null
     *
     * @param id the id of the petType to save.
     * @param petType the petType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated petType,
     * or with status {@code 400 (Bad Request)} if the petType is not valid,
     * or with status {@code 404 (Not Found)} if the petType is not found,
     * or with status {@code 500 (Internal Server Error)} if the petType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PetType> partialUpdatePetType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PetType petType
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PetType partially : {}, {}", id, petType);
        if (petType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, petType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!petTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PetType> result = petTypeService.partialUpdate(petType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, petType.getId().toString())
        );
    }

    /**
     * {@code GET  /pet-types} : get all the petTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of petTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PetType>> getAllPetTypes(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of PetTypes");
        Page<PetType> page = petTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pet-types/:id} : get the "id" petType.
     *
     * @param id the id of the petType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the petType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetType> getPetType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PetType : {}", id);
        Optional<PetType> petType = petTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(petType);
    }

    /**
     * {@code DELETE  /pet-types/:id} : delete the "id" petType.
     *
     * @param id the id of the petType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePetType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PetType : {}", id);
        petTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
