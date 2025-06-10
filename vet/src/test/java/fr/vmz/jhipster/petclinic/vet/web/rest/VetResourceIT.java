package fr.vmz.jhipster.petclinic.vet.web.rest;

import static fr.vmz.jhipster.petclinic.vet.domain.VetAsserts.*;
import static fr.vmz.jhipster.petclinic.vet.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vmz.jhipster.petclinic.vet.IntegrationTest;
import fr.vmz.jhipster.petclinic.vet.domain.Specialty;
import fr.vmz.jhipster.petclinic.vet.domain.Vet;
import fr.vmz.jhipster.petclinic.vet.repository.VetRepository;
import fr.vmz.jhipster.petclinic.vet.service.VetService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VetResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VetRepository vetRepository;

    @Mock
    private VetRepository vetRepositoryMock;

    @Mock
    private VetService vetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVetMockMvc;

    private Vet vet;

    private Vet insertedVet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createEntity() {
        return new Vet().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createUpdatedEntity() {
        return new Vet().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
    }

    @BeforeEach
    void initTest() {
        vet = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedVet != null) {
            vetRepository.delete(insertedVet);
            insertedVet = null;
        }
    }

    @Test
    @Transactional
    void createVet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vet
        var returnedVet = om.readValue(
            restVetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Vet.class
        );

        // Validate the Vet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertVetUpdatableFieldsEquals(returnedVet, getPersistedVet(returnedVet));

        insertedVet = returnedVet;
    }

    @Test
    @Transactional
    void createVetWithExistingId() throws Exception {
        // Create the Vet with an existing ID
        vet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vet)))
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vet.setFirstName(null);

        // Create the Vet, which fails.

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vet)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        vet.setLastName(null);

        // Create the Vet, which fails.

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vet)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVets() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vet.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vetRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVet() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get the vet
        restVetMockMvc
            .perform(get(ENTITY_API_URL_ID, vet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vet.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getVetsByIdFiltering() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        Long id = vet.getId();

        defaultVetFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultVetFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultVetFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName equals to
        defaultVetFiltering("firstName.equals=" + DEFAULT_FIRST_NAME, "firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName in
        defaultVetFiltering("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME, "firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName is not null
        defaultVetFiltering("firstName.specified=true", "firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName contains
        defaultVetFiltering("firstName.contains=" + DEFAULT_FIRST_NAME, "firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName does not contain
        defaultVetFiltering("firstName.doesNotContain=" + UPDATED_FIRST_NAME, "firstName.doesNotContain=" + DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName equals to
        defaultVetFiltering("lastName.equals=" + DEFAULT_LAST_NAME, "lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName in
        defaultVetFiltering("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME, "lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName is not null
        defaultVetFiltering("lastName.specified=true", "lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllVetsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName contains
        defaultVetFiltering("lastName.contains=" + DEFAULT_LAST_NAME, "lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName does not contain
        defaultVetFiltering("lastName.doesNotContain=" + UPDATED_LAST_NAME, "lastName.doesNotContain=" + DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsBySpecialtiesIsEqualToSomething() throws Exception {
        Specialty specialties;
        if (TestUtil.findAll(em, Specialty.class).isEmpty()) {
            vetRepository.saveAndFlush(vet);
            specialties = SpecialtyResourceIT.createEntity();
        } else {
            specialties = TestUtil.findAll(em, Specialty.class).get(0);
        }
        em.persist(specialties);
        em.flush();
        vet.addSpecialties(specialties);
        vetRepository.saveAndFlush(vet);
        Long specialtiesId = specialties.getId();
        // Get all the vetList where specialties equals to specialtiesId
        defaultVetShouldBeFound("specialtiesId.equals=" + specialtiesId);

        // Get all the vetList where specialties equals to (specialtiesId + 1)
        defaultVetShouldNotBeFound("specialtiesId.equals=" + (specialtiesId + 1));
    }

    private void defaultVetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultVetShouldBeFound(shouldBeFound);
        defaultVetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVetShouldBeFound(String filter) throws Exception {
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vet.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));

        // Check, that the count call also returns 1
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVetShouldNotBeFound(String filter) throws Exception {
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVet() throws Exception {
        // Get the vet
        restVetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVet() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vet
        Vet updatedVet = vetRepository.findById(vet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVet are not directly saved in db
        em.detach(updatedVet);
        updatedVet.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVetToMatchAllProperties(updatedVet);
    }

    @Test
    @Transactional
    void putNonExistingVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(put(ENTITY_API_URL_ID, vet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vet)))
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVetWithPatch() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVet, vet), getPersistedVet(vet));
    }

    @Test
    @Transactional
    void fullUpdateVetWithPatch() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        partialUpdatedVet.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVetUpdatableFieldsEquals(partialUpdatedVet, getPersistedVet(partialUpdatedVet));
    }

    @Test
    @Transactional
    void patchNonExistingVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(patch(ENTITY_API_URL_ID, vet.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vet)))
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVet() throws Exception {
        // Initialize the database
        insertedVet = vetRepository.saveAndFlush(vet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vet
        restVetMockMvc.perform(delete(ENTITY_API_URL_ID, vet.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vetRepository.count();
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

    protected Vet getPersistedVet(Vet vet) {
        return vetRepository.findById(vet.getId()).orElseThrow();
    }

    protected void assertPersistedVetToMatchAllProperties(Vet expectedVet) {
        assertVetAllPropertiesEquals(expectedVet, getPersistedVet(expectedVet));
    }

    protected void assertPersistedVetToMatchUpdatableProperties(Vet expectedVet) {
        assertVetAllUpdatablePropertiesEquals(expectedVet, getPersistedVet(expectedVet));
    }
}
