package fr.vmz.jhipster.petclinic.vet.web.rest;

import static fr.vmz.jhipster.petclinic.vet.domain.SpecialtyAsserts.*;
import static fr.vmz.jhipster.petclinic.vet.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vmz.jhipster.petclinic.vet.IntegrationTest;
import fr.vmz.jhipster.petclinic.vet.domain.Specialty;
import fr.vmz.jhipster.petclinic.vet.repository.SpecialtyRepository;
import fr.vmz.jhipster.petclinic.vet.service.dto.SpecialtyDTO;
import fr.vmz.jhipster.petclinic.vet.service.mapper.SpecialtyMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SpecialtyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SpecialtyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/specialties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    private Specialty insertedSpecialty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createEntity() {
        return new Specialty().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createUpdatedEntity() {
        return new Specialty().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        specialty = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSpecialty != null) {
            specialtyRepository.delete(insertedSpecialty);
            insertedSpecialty = null;
        }
    }

    @Test
    @Transactional
    void createSpecialty() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);
        var returnedSpecialtyDTO = om.readValue(
            restSpecialtyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialtyDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SpecialtyDTO.class
        );

        // Validate the Specialty in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSpecialty = specialtyMapper.toEntity(returnedSpecialtyDTO);
        assertSpecialtyUpdatableFieldsEquals(returnedSpecialty, getPersistedSpecialty(returnedSpecialty));

        insertedSpecialty = returnedSpecialty;
    }

    @Test
    @Transactional
    void createSpecialtyWithExistingId() throws Exception {
        // Create the Specialty with an existing ID
        specialty.setId(1L);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialtyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        specialty.setName(null);

        // Create the Specialty, which fails.
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        restSpecialtyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialtyDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSpecialties() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getSpecialty() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc
            .perform(get(ENTITY_API_URL_ID, specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSpecialty() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialty
        Specialty updatedSpecialty = specialtyRepository.findById(specialty.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSpecialty are not directly saved in db
        em.detach(updatedSpecialty);
        updatedSpecialty.name(UPDATED_NAME);
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(updatedSpecialty);

        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialtyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialtyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSpecialtyToMatchAllProperties(updatedSpecialty);
    }

    @Test
    @Transactional
    void putNonExistingSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, specialtyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(specialtyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSpecialtyWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialty using partial update
        Specialty partialUpdatedSpecialty = new Specialty();
        partialUpdatedSpecialty.setId(specialty.getId());

        partialUpdatedSpecialty.name(UPDATED_NAME);

        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialty))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialtyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSpecialty, specialty),
            getPersistedSpecialty(specialty)
        );
    }

    @Test
    @Transactional
    void fullUpdateSpecialtyWithPatch() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the specialty using partial update
        Specialty partialUpdatedSpecialty = new Specialty();
        partialUpdatedSpecialty.setId(specialty.getId());

        partialUpdatedSpecialty.name(UPDATED_NAME);

        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSpecialty.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSpecialty))
            )
            .andExpect(status().isOk());

        // Validate the Specialty in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSpecialtyUpdatableFieldsEquals(partialUpdatedSpecialty, getPersistedSpecialty(partialUpdatedSpecialty));
    }

    @Test
    @Transactional
    void patchNonExistingSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, specialtyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(specialtyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSpecialty() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        specialty.setId(longCount.incrementAndGet());

        // Create the Specialty
        SpecialtyDTO specialtyDTO = specialtyMapper.toDto(specialty);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(specialtyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Specialty in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSpecialty() throws Exception {
        // Initialize the database
        insertedSpecialty = specialtyRepository.saveAndFlush(specialty);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the specialty
        restSpecialtyMockMvc
            .perform(delete(ENTITY_API_URL_ID, specialty.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return specialtyRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Specialty getPersistedSpecialty(Specialty specialty) {
        return specialtyRepository.findById(specialty.getId()).orElseThrow();
    }

    protected void assertPersistedSpecialtyToMatchAllProperties(Specialty expectedSpecialty) {
        assertSpecialtyAllPropertiesEquals(expectedSpecialty, getPersistedSpecialty(expectedSpecialty));
    }

    protected void assertPersistedSpecialtyToMatchUpdatableProperties(Specialty expectedSpecialty) {
        assertSpecialtyAllUpdatablePropertiesEquals(expectedSpecialty, getPersistedSpecialty(expectedSpecialty));
    }
}
