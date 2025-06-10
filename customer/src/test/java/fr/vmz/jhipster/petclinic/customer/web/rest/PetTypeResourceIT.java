package fr.vmz.jhipster.petclinic.customer.web.rest;

import static fr.vmz.jhipster.petclinic.customer.domain.PetTypeAsserts.*;
import static fr.vmz.jhipster.petclinic.customer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vmz.jhipster.petclinic.customer.IntegrationTest;
import fr.vmz.jhipster.petclinic.customer.domain.PetType;
import fr.vmz.jhipster.petclinic.customer.repository.PetTypeRepository;
import fr.vmz.jhipster.petclinic.customer.service.dto.PetTypeDTO;
import fr.vmz.jhipster.petclinic.customer.service.mapper.PetTypeMapper;
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
 * Integration tests for the {@link PetTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PetTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pet-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PetTypeRepository petTypeRepository;

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetTypeMockMvc;

    private PetType petType;

    private PetType insertedPetType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PetType createEntity() {
        return new PetType().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PetType createUpdatedEntity() {
        return new PetType().name(UPDATED_NAME);
    }

    @BeforeEach
    void initTest() {
        petType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPetType != null) {
            petTypeRepository.delete(insertedPetType);
            insertedPetType = null;
        }
    }

    @Test
    @Transactional
    void createPetType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);
        var returnedPetTypeDTO = om.readValue(
            restPetTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PetTypeDTO.class
        );

        // Validate the PetType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPetType = petTypeMapper.toEntity(returnedPetTypeDTO);
        assertPetTypeUpdatableFieldsEquals(returnedPetType, getPersistedPetType(returnedPetType));

        insertedPetType = returnedPetType;
    }

    @Test
    @Transactional
    void createPetTypeWithExistingId() throws Exception {
        // Create the PetType with an existing ID
        petType.setId(1L);
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        petType.setName(null);

        // Create the PetType, which fails.
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        restPetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPetTypes() throws Exception {
        // Initialize the database
        insertedPetType = petTypeRepository.saveAndFlush(petType);

        // Get all the petTypeList
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(petType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getPetType() throws Exception {
        // Initialize the database
        insertedPetType = petTypeRepository.saveAndFlush(petType);

        // Get the petType
        restPetTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, petType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(petType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingPetType() throws Exception {
        // Get the petType
        restPetTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPetType() throws Exception {
        // Initialize the database
        insertedPetType = petTypeRepository.saveAndFlush(petType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the petType
        PetType updatedPetType = petTypeRepository.findById(petType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPetType are not directly saved in db
        em.detach(updatedPetType);
        updatedPetType.name(UPDATED_NAME);
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(updatedPetType);

        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petTypeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPetTypeToMatchAllProperties(updatedPetType);
    }

    @Test
    @Transactional
    void putNonExistingPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, petTypeDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPetType = petTypeRepository.saveAndFlush(petType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        partialUpdatedPetType.name(UPDATED_NAME);

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPetTypeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPetType, petType), getPersistedPetType(petType));
    }

    @Test
    @Transactional
    void fullUpdatePetTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPetType = petTypeRepository.saveAndFlush(petType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the petType using partial update
        PetType partialUpdatedPetType = new PetType();
        partialUpdatedPetType.setId(petType.getId());

        partialUpdatedPetType.name(UPDATED_NAME);

        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPetType))
            )
            .andExpect(status().isOk());

        // Validate the PetType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPetTypeUpdatableFieldsEquals(partialUpdatedPetType, getPersistedPetType(partialUpdatedPetType));
    }

    @Test
    @Transactional
    void patchNonExistingPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, petTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(petTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPetType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        petType.setId(longCount.incrementAndGet());

        // Create the PetType
        PetTypeDTO petTypeDTO = petTypeMapper.toDto(petType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(petTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PetType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePetType() throws Exception {
        // Initialize the database
        insertedPetType = petTypeRepository.saveAndFlush(petType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the petType
        restPetTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, petType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return petTypeRepository.count();
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

    protected PetType getPersistedPetType(PetType petType) {
        return petTypeRepository.findById(petType.getId()).orElseThrow();
    }

    protected void assertPersistedPetTypeToMatchAllProperties(PetType expectedPetType) {
        assertPetTypeAllPropertiesEquals(expectedPetType, getPersistedPetType(expectedPetType));
    }

    protected void assertPersistedPetTypeToMatchUpdatableProperties(PetType expectedPetType) {
        assertPetTypeAllUpdatablePropertiesEquals(expectedPetType, getPersistedPetType(expectedPetType));
    }
}
