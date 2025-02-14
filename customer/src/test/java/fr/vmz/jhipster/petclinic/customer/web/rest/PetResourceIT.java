package fr.vmz.jhipster.petclinic.customer.web.rest;

import static fr.vmz.jhipster.petclinic.customer.domain.PetAsserts.*;
import static fr.vmz.jhipster.petclinic.customer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vmz.jhipster.petclinic.customer.IntegrationTest;
import fr.vmz.jhipster.petclinic.customer.domain.Pet;
import fr.vmz.jhipster.petclinic.customer.repository.PetRepository;
import fr.vmz.jhipster.petclinic.customer.service.PetService;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/pets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PetRepository petRepository;

    @Mock
    private PetRepository petRepositoryMock;

    @Mock
    private PetService petServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetMockMvc;

    private Pet pet;

    private Pet insertedPet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createEntity() {
        return new Pet().name(DEFAULT_NAME).birthDate(DEFAULT_BIRTH_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createUpdatedEntity() {
        return new Pet().name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);
    }

    @BeforeEach
    public void initTest() {
        pet = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPet != null) {
            petRepository.delete(insertedPet);
            insertedPet = null;
        }
    }

    @Test
    @Transactional
    void createPet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pet
        var returnedPet = om.readValue(
            restPetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pet)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Pet.class
        );

        // Validate the Pet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPetUpdatableFieldsEquals(returnedPet, getPersistedPet(returnedPet));

        insertedPet = returnedPet;
    }

    @Test
    @Transactional
    void createPetWithExistingId() throws Exception {
        // Create the Pet with an existing ID
        pet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pet)))
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pet.setName(null);

        // Create the Pet, which fails.

        restPetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pet)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPets() throws Exception {
        // Initialize the database
        insertedPet = petRepository.saveAndFlush(pet);

        // Get all the petList
        restPetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(petServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(petServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(petServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(petRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPet() throws Exception {
        // Initialize the database
        insertedPet = petRepository.saveAndFlush(pet);

        // Get the pet
        restPetMockMvc
            .perform(get(ENTITY_API_URL_ID, pet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPet() throws Exception {
        // Get the pet
        restPetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPet() throws Exception {
        // Initialize the database
        insertedPet = petRepository.saveAndFlush(pet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPet are not directly saved in db
        em.detach(updatedPet);
        updatedPet.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);

        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPetToMatchAllProperties(updatedPet);
    }

    @Test
    @Transactional
    void putNonExistingPet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(put(ENTITY_API_URL_ID, pet.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pet)))
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetWithPatch() throws Exception {
        // Initialize the database
        insertedPet = petRepository.saveAndFlush(pet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pet using partial update
        Pet partialUpdatedPet = new Pet();
        partialUpdatedPet.setId(pet.getId());

        partialUpdatedPet.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);

        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPetUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPet, pet), getPersistedPet(pet));
    }

    @Test
    @Transactional
    void fullUpdatePetWithPatch() throws Exception {
        // Initialize the database
        insertedPet = petRepository.saveAndFlush(pet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pet using partial update
        Pet partialUpdatedPet = new Pet();
        partialUpdatedPet.setId(pet.getId());

        partialUpdatedPet.name(UPDATED_NAME).birthDate(UPDATED_BIRTH_DATE);

        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPetUpdatableFieldsEquals(partialUpdatedPet, getPersistedPet(partialUpdatedPet));
    }

    @Test
    @Transactional
    void patchNonExistingPet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(patch(ENTITY_API_URL_ID, pet.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pet)))
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePet() throws Exception {
        // Initialize the database
        insertedPet = petRepository.saveAndFlush(pet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pet
        restPetMockMvc.perform(delete(ENTITY_API_URL_ID, pet.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return petRepository.count();
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

    protected Pet getPersistedPet(Pet pet) {
        return petRepository.findById(pet.getId()).orElseThrow();
    }

    protected void assertPersistedPetToMatchAllProperties(Pet expectedPet) {
        assertPetAllPropertiesEquals(expectedPet, getPersistedPet(expectedPet));
    }

    protected void assertPersistedPetToMatchUpdatableProperties(Pet expectedPet) {
        assertPetAllUpdatablePropertiesEquals(expectedPet, getPersistedPet(expectedPet));
    }
}
