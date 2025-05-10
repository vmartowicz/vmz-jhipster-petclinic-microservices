package fr.vmz.jhipster.petclinic.visit.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.vmz.jhipster.petclinic.visit.IntegrationTest;
import fr.vmz.jhipster.petclinic.visit.domain.Visit;
import fr.vmz.jhipster.petclinic.visit.repository.VisitRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static fr.vmz.jhipster.petclinic.visit.domain.VisitAsserts.*;
import static fr.vmz.jhipster.petclinic.visit.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VisitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VisitExtendedResourceIT {

    private static final LocalDate DEFAULT_VISIT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VISIT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PET_ID = 1L;
    private static final Long UPDATED_PET_ID = 2L;

    private static final String ENTITY_API_URL = "/api/visits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVisitMockMvc;

    private Visit visit;

    private Visit insertedVisit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Visit createEntity() {
        return new Visit().visitDate(DEFAULT_VISIT_DATE).description(DEFAULT_DESCRIPTION).petId(DEFAULT_PET_ID);
    }

    @BeforeEach
    public void initTest() {
        visit = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVisit != null) {
            visitRepository.delete(insertedVisit);
            insertedVisit = null;
        }
    }
    @Test
    @Transactional
    void getAllVisitsByPetId() throws Exception {
        // Initialize the database
        insertedVisit = visitRepository.saveAndFlush(visit);

        // Get all the visitList by petIds
        restVisitMockMvc
            .perform(get(ENTITY_API_URL + "/pets")
                .param("petIds", DEFAULT_PET_ID.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].petId").value(hasItem(DEFAULT_PET_ID.intValue())));
    }


}
