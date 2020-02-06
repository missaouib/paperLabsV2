package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.PaperlabsApp;
import com.apeiron.paperlabs.domain.Employer;
import com.apeiron.paperlabs.repository.EmployerRepository;
import com.apeiron.paperlabs.repository.search.EmployerSearchRepository;
import com.apeiron.paperlabs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.Collections;
import java.util.List;

import static com.apeiron.paperlabs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EmployerResource} REST controller.
 */
@SpringBootTest(classes = PaperlabsApp.class)
public class EmployerResourceIT {

    private static final Long DEFAULT_EMPLOYER_CIN_NUMBER = 1L;
    private static final Long UPDATED_EMPLOYER_CIN_NUMBER = 2L;
    private static final Long SMALLER_EMPLOYER_CIN_NUMBER = 1L - 1L;

    private static final String DEFAULT_EMPLOYER_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMPLOYER_FULL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYER_FULL_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private EmployerRepository employerRepository;

    /**
     * This repository is mocked in the com.apeiron.paperlabs.repository.search test package.
     *
     * @see com.apeiron.paperlabs.repository.search.EmployerSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmployerSearchRepository mockEmployerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restEmployerMockMvc;

    private Employer employer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmployerResource employerResource = new EmployerResource(employerRepository, mockEmployerSearchRepository);
        this.restEmployerMockMvc = MockMvcBuilders.standaloneSetup(employerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createEntity() {
        Employer employer = new Employer()
            .employerCinNumber(DEFAULT_EMPLOYER_CIN_NUMBER)
            .employerFullName(DEFAULT_EMPLOYER_FULL_NAME)
            .employerFullAddress(DEFAULT_EMPLOYER_FULL_ADDRESS);
        return employer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employer createUpdatedEntity() {
        Employer employer = new Employer()
            .employerCinNumber(UPDATED_EMPLOYER_CIN_NUMBER)
            .employerFullName(UPDATED_EMPLOYER_FULL_NAME)
            .employerFullAddress(UPDATED_EMPLOYER_FULL_ADDRESS);
        return employer;
    }

    @BeforeEach
    public void initTest() {
        employerRepository.deleteAll();
        employer = createEntity();
    }

    @Test
    public void createEmployer() throws Exception {
        int databaseSizeBeforeCreate = employerRepository.findAll().size();

        // Create the Employer
        restEmployerMockMvc.perform(post("/api/employers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employer)))
            .andExpect(status().isCreated());

        // Validate the Employer in the database
        List<Employer> employerList = employerRepository.findAll();
        assertThat(employerList).hasSize(databaseSizeBeforeCreate + 1);
        Employer testEmployer = employerList.get(employerList.size() - 1);
        assertThat(testEmployer.getEmployerCinNumber()).isEqualTo(DEFAULT_EMPLOYER_CIN_NUMBER);
        assertThat(testEmployer.getEmployerFullName()).isEqualTo(DEFAULT_EMPLOYER_FULL_NAME);
        assertThat(testEmployer.getEmployerFullAddress()).isEqualTo(DEFAULT_EMPLOYER_FULL_ADDRESS);

        // Validate the Employer in Elasticsearch
        verify(mockEmployerSearchRepository, times(1)).save(testEmployer);
    }

    @Test
    public void createEmployerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = employerRepository.findAll().size();

        // Create the Employer with an existing ID
        employer.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployerMockMvc.perform(post("/api/employers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employer)))
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        List<Employer> employerList = employerRepository.findAll();
        assertThat(employerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Employer in Elasticsearch
        verify(mockEmployerSearchRepository, times(0)).save(employer);
    }


    @Test
    public void getAllEmployers() throws Exception {
        // Initialize the database
        employerRepository.save(employer);

        // Get all the employerList
        restEmployerMockMvc.perform(get("/api/employers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId())))
            .andExpect(jsonPath("$.[*].employerCinNumber").value(hasItem(DEFAULT_EMPLOYER_CIN_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].employerFullName").value(hasItem(DEFAULT_EMPLOYER_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].employerFullAddress").value(hasItem(DEFAULT_EMPLOYER_FULL_ADDRESS.toString())));
    }
    
    @Test
    public void getEmployer() throws Exception {
        // Initialize the database
        employerRepository.save(employer);

        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", employer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(employer.getId()))
            .andExpect(jsonPath("$.employerCinNumber").value(DEFAULT_EMPLOYER_CIN_NUMBER.intValue()))
            .andExpect(jsonPath("$.employerFullName").value(DEFAULT_EMPLOYER_FULL_NAME.toString()))
            .andExpect(jsonPath("$.employerFullAddress").value(DEFAULT_EMPLOYER_FULL_ADDRESS.toString()));
    }

    @Test
    public void getNonExistingEmployer() throws Exception {
        // Get the employer
        restEmployerMockMvc.perform(get("/api/employers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateEmployer() throws Exception {
        // Initialize the database
        employerRepository.save(employer);

        int databaseSizeBeforeUpdate = employerRepository.findAll().size();

        // Update the employer
        Employer updatedEmployer = employerRepository.findById(employer.getId()).get();
        updatedEmployer
            .employerCinNumber(UPDATED_EMPLOYER_CIN_NUMBER)
            .employerFullName(UPDATED_EMPLOYER_FULL_NAME)
            .employerFullAddress(UPDATED_EMPLOYER_FULL_ADDRESS);

        restEmployerMockMvc.perform(put("/api/employers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmployer)))
            .andExpect(status().isOk());

        // Validate the Employer in the database
        List<Employer> employerList = employerRepository.findAll();
        assertThat(employerList).hasSize(databaseSizeBeforeUpdate);
        Employer testEmployer = employerList.get(employerList.size() - 1);
        assertThat(testEmployer.getEmployerCinNumber()).isEqualTo(UPDATED_EMPLOYER_CIN_NUMBER);
        assertThat(testEmployer.getEmployerFullName()).isEqualTo(UPDATED_EMPLOYER_FULL_NAME);
        assertThat(testEmployer.getEmployerFullAddress()).isEqualTo(UPDATED_EMPLOYER_FULL_ADDRESS);

        // Validate the Employer in Elasticsearch
        verify(mockEmployerSearchRepository, times(1)).save(testEmployer);
    }

    @Test
    public void updateNonExistingEmployer() throws Exception {
        int databaseSizeBeforeUpdate = employerRepository.findAll().size();

        // Create the Employer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployerMockMvc.perform(put("/api/employers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(employer)))
            .andExpect(status().isBadRequest());

        // Validate the Employer in the database
        List<Employer> employerList = employerRepository.findAll();
        assertThat(employerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employer in Elasticsearch
        verify(mockEmployerSearchRepository, times(0)).save(employer);
    }

    @Test
    public void deleteEmployer() throws Exception {
        // Initialize the database
        employerRepository.save(employer);

        int databaseSizeBeforeDelete = employerRepository.findAll().size();

        // Delete the employer
        restEmployerMockMvc.perform(delete("/api/employers/{id}", employer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employer> employerList = employerRepository.findAll();
        assertThat(employerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Employer in Elasticsearch
        verify(mockEmployerSearchRepository, times(1)).deleteById(employer.getId());
    }

    @Test
    public void searchEmployer() throws Exception {
        // Initialize the database
        employerRepository.save(employer);
        when(mockEmployerSearchRepository.search(queryStringQuery("id:" + employer.getId())))
            .thenReturn(Collections.singletonList(employer));
        // Search the employer
        restEmployerMockMvc.perform(get("/api/_search/employers?query=id:" + employer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employer.getId())))
            .andExpect(jsonPath("$.[*].employerCinNumber").value(hasItem(DEFAULT_EMPLOYER_CIN_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].employerFullName").value(hasItem(DEFAULT_EMPLOYER_FULL_NAME)))
            .andExpect(jsonPath("$.[*].employerFullAddress").value(hasItem(DEFAULT_EMPLOYER_FULL_ADDRESS)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employer.class);
        Employer employer1 = new Employer();
        employer1.setId("id1");
        Employer employer2 = new Employer();
        employer2.setId(employer1.getId());
        assertThat(employer1).isEqualTo(employer2);
        employer2.setId("id2");
        assertThat(employer1).isNotEqualTo(employer2);
        employer1.setId(null);
        assertThat(employer1).isNotEqualTo(employer2);
    }
}
