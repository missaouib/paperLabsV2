package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.PaperlabsApp;
import com.apeiron.paperlabs.domain.Lawyer;
import com.apeiron.paperlabs.repository.LawyerRepository;
import com.apeiron.paperlabs.repository.search.LawyerSearchRepository;
import com.apeiron.paperlabs.service.LawyerService;
import com.apeiron.paperlabs.service.dto.LawyerDTO;
import com.apeiron.paperlabs.service.mapper.LawyerMapper;
import com.apeiron.paperlabs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
 * Integration tests for the {@link LawyerResource} REST controller.
 */
@SpringBootTest(classes = PaperlabsApp.class)
public class LawyerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private LawyerRepository lawyerRepository;

    @Autowired
    private LawyerMapper lawyerMapper;

    @Autowired
    private LawyerService lawyerService;

    /**
     * This repository is mocked in the com.apeiron.paperlabs.repository.search test package.
     *
     * @see com.apeiron.paperlabs.repository.search.LawyerSearchRepositoryMockConfiguration
     */
    @Autowired
    private LawyerSearchRepository mockLawyerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restLawyerMockMvc;

    private Lawyer lawyer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LawyerResource lawyerResource = new LawyerResource(lawyerService);
        this.restLawyerMockMvc = MockMvcBuilders.standaloneSetup(lawyerResource)
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
    public static Lawyer createEntity() {
        Lawyer lawyer = new Lawyer()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .mobile(DEFAULT_MOBILE)
            .tel(DEFAULT_TEL)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS);
        return lawyer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lawyer createUpdatedEntity() {
        Lawyer lawyer = new Lawyer()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .mobile(UPDATED_MOBILE)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS);
        return lawyer;
    }

    @BeforeEach
    public void initTest() {
        lawyerRepository.deleteAll();
        lawyer = createEntity();
    }

    @Test
    public void createLawyer() throws Exception {
        int databaseSizeBeforeCreate = lawyerRepository.findAll().size();

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);
        restLawyerMockMvc.perform(post("/api/lawyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawyerDTO)))
            .andExpect(status().isCreated());

        // Validate the Lawyer in the database
        List<Lawyer> lawyerList = lawyerRepository.findAll();
        assertThat(lawyerList).hasSize(databaseSizeBeforeCreate + 1);
        Lawyer testLawyer = lawyerList.get(lawyerList.size() - 1);
        assertThat(testLawyer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testLawyer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testLawyer.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testLawyer.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testLawyer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLawyer.getAddress()).isEqualTo(DEFAULT_ADDRESS);

        // Validate the Lawyer in Elasticsearch
        verify(mockLawyerSearchRepository, times(1)).save(testLawyer);
    }

    @Test
    public void createLawyerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lawyerRepository.findAll().size();

        // Create the Lawyer with an existing ID
        lawyer.setId("existing_id");
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLawyerMockMvc.perform(post("/api/lawyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawyerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lawyer in the database
        List<Lawyer> lawyerList = lawyerRepository.findAll();
        assertThat(lawyerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Lawyer in Elasticsearch
        verify(mockLawyerSearchRepository, times(0)).save(lawyer);
    }


    @Test
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lawyerRepository.findAll().size();
        // set the field null
        lawyer.setFirstName(null);

        // Create the Lawyer, which fails.
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        restLawyerMockMvc.perform(post("/api/lawyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawyerDTO)))
            .andExpect(status().isBadRequest());

        List<Lawyer> lawyerList = lawyerRepository.findAll();
        assertThat(lawyerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lawyerRepository.findAll().size();
        // set the field null
        lawyer.setLastName(null);

        // Create the Lawyer, which fails.
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        restLawyerMockMvc.perform(post("/api/lawyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawyerDTO)))
            .andExpect(status().isBadRequest());

        List<Lawyer> lawyerList = lawyerRepository.findAll();
        assertThat(lawyerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllLawyers() throws Exception {
        // Initialize the database
        lawyerRepository.save(lawyer);

        // Get all the lawyerList
        restLawyerMockMvc.perform(get("/api/lawyers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawyer.getId())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }
    
    @Test
    public void getLawyer() throws Exception {
        // Initialize the database
        lawyerRepository.save(lawyer);

        // Get the lawyer
        restLawyerMockMvc.perform(get("/api/lawyers/{id}", lawyer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lawyer.getId()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    public void getNonExistingLawyer() throws Exception {
        // Get the lawyer
        restLawyerMockMvc.perform(get("/api/lawyers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLawyer() throws Exception {
        // Initialize the database
        lawyerRepository.save(lawyer);

        int databaseSizeBeforeUpdate = lawyerRepository.findAll().size();

        // Update the lawyer
        Lawyer updatedLawyer = lawyerRepository.findById(lawyer.getId()).get();
        updatedLawyer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .mobile(UPDATED_MOBILE)
            .tel(UPDATED_TEL)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS);
        LawyerDTO lawyerDTO = lawyerMapper.toDto(updatedLawyer);

        restLawyerMockMvc.perform(put("/api/lawyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawyerDTO)))
            .andExpect(status().isOk());

        // Validate the Lawyer in the database
        List<Lawyer> lawyerList = lawyerRepository.findAll();
        assertThat(lawyerList).hasSize(databaseSizeBeforeUpdate);
        Lawyer testLawyer = lawyerList.get(lawyerList.size() - 1);
        assertThat(testLawyer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testLawyer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testLawyer.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testLawyer.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testLawyer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLawyer.getAddress()).isEqualTo(UPDATED_ADDRESS);

        // Validate the Lawyer in Elasticsearch
        verify(mockLawyerSearchRepository, times(1)).save(testLawyer);
    }

    @Test
    public void updateNonExistingLawyer() throws Exception {
        int databaseSizeBeforeUpdate = lawyerRepository.findAll().size();

        // Create the Lawyer
        LawyerDTO lawyerDTO = lawyerMapper.toDto(lawyer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLawyerMockMvc.perform(put("/api/lawyers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lawyerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Lawyer in the database
        List<Lawyer> lawyerList = lawyerRepository.findAll();
        assertThat(lawyerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lawyer in Elasticsearch
        verify(mockLawyerSearchRepository, times(0)).save(lawyer);
    }

    @Test
    public void deleteLawyer() throws Exception {
        // Initialize the database
        lawyerRepository.save(lawyer);

        int databaseSizeBeforeDelete = lawyerRepository.findAll().size();

        // Delete the lawyer
        restLawyerMockMvc.perform(delete("/api/lawyers/{id}", lawyer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lawyer> lawyerList = lawyerRepository.findAll();
        assertThat(lawyerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Lawyer in Elasticsearch
        verify(mockLawyerSearchRepository, times(1)).deleteById(lawyer.getId());
    }

    @Test
    public void searchLawyer() throws Exception {
        // Initialize the database
        lawyerRepository.save(lawyer);
        when(mockLawyerSearchRepository.search(queryStringQuery("id:" + lawyer.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lawyer), PageRequest.of(0, 1), 1));
        // Search the lawyer
        restLawyerMockMvc.perform(get("/api/_search/lawyers?query=id:" + lawyer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lawyer.getId())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lawyer.class);
        Lawyer lawyer1 = new Lawyer();
        lawyer1.setId("id1");
        Lawyer lawyer2 = new Lawyer();
        lawyer2.setId(lawyer1.getId());
        assertThat(lawyer1).isEqualTo(lawyer2);
        lawyer2.setId("id2");
        assertThat(lawyer1).isNotEqualTo(lawyer2);
        lawyer1.setId(null);
        assertThat(lawyer1).isNotEqualTo(lawyer2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LawyerDTO.class);
        LawyerDTO lawyerDTO1 = new LawyerDTO();
        lawyerDTO1.setId("id1");
        LawyerDTO lawyerDTO2 = new LawyerDTO();
        assertThat(lawyerDTO1).isNotEqualTo(lawyerDTO2);
        lawyerDTO2.setId(lawyerDTO1.getId());
        assertThat(lawyerDTO1).isEqualTo(lawyerDTO2);
        lawyerDTO2.setId("id2");
        assertThat(lawyerDTO1).isNotEqualTo(lawyerDTO2);
        lawyerDTO1.setId(null);
        assertThat(lawyerDTO1).isNotEqualTo(lawyerDTO2);
    }
}
