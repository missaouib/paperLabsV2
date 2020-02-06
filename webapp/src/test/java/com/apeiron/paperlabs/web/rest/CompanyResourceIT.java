package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.PaperlabsApp;
import com.apeiron.paperlabs.domain.Company;
import com.apeiron.paperlabs.repository.CompanyRepository;
import com.apeiron.paperlabs.repository.search.CompanySearchRepository;
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
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@SpringBootTest(classes = PaperlabsApp.class)
public class CompanyResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COMPANY_CAPITAL = 1L;
    private static final Long UPDATED_COMPANY_CAPITAL = 2L;
    private static final Long SMALLER_COMPANY_CAPITAL = 1L - 1L;

    private static final String DEFAULT_COMPANY_UNIQUE_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_UNIQUE_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_REPRESENTATIVE_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_REPRESENTATIVE_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_FULL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_FULL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_TYPE = "BBBBBBBBBB";

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * This repository is mocked in the com.apeiron.paperlabs.repository.search test package.
     *
     * @see com.apeiron.paperlabs.repository.search.CompanySearchRepositoryMockConfiguration
     */
    @Autowired
    private CompanySearchRepository mockCompanySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restCompanyMockMvc;

    private Company company;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompanyResource companyResource = new CompanyResource(companyRepository, mockCompanySearchRepository);
        this.restCompanyMockMvc = MockMvcBuilders.standaloneSetup(companyResource)
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
    public static Company createEntity() {
        Company company = new Company()
            .companyName(DEFAULT_COMPANY_NAME)
            .companyCapital(DEFAULT_COMPANY_CAPITAL)
            .companyUniqueIdentification(DEFAULT_COMPANY_UNIQUE_IDENTIFICATION)
            .companyRepresentativeFullName(DEFAULT_COMPANY_REPRESENTATIVE_FULL_NAME)
            .companyFullAddress(DEFAULT_COMPANY_FULL_ADDRESS)
            .companyType(DEFAULT_COMPANY_TYPE);
        return company;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity() {
        Company company = new Company()
            .companyName(UPDATED_COMPANY_NAME)
            .companyCapital(UPDATED_COMPANY_CAPITAL)
            .companyUniqueIdentification(UPDATED_COMPANY_UNIQUE_IDENTIFICATION)
            .companyRepresentativeFullName(UPDATED_COMPANY_REPRESENTATIVE_FULL_NAME)
            .companyFullAddress(UPDATED_COMPANY_FULL_ADDRESS)
            .companyType(UPDATED_COMPANY_TYPE);
        return company;
    }

    @BeforeEach
    public void initTest() {
        companyRepository.deleteAll();
        company = createEntity();
    }

    @Test
    public void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompany.getCompanyCapital()).isEqualTo(DEFAULT_COMPANY_CAPITAL);
        assertThat(testCompany.getCompanyUniqueIdentification()).isEqualTo(DEFAULT_COMPANY_UNIQUE_IDENTIFICATION);
        assertThat(testCompany.getCompanyRepresentativeFullName()).isEqualTo(DEFAULT_COMPANY_REPRESENTATIVE_FULL_NAME);
        assertThat(testCompany.getCompanyFullAddress()).isEqualTo(DEFAULT_COMPANY_FULL_ADDRESS);
        assertThat(testCompany.getCompanyType()).isEqualTo(DEFAULT_COMPANY_TYPE);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    public void createCompanyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // Create the Company with an existing ID
        company.setId("existing_id");

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc.perform(post("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }


    @Test
    public void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.save(company);

        // Get all the companyList
        restCompanyMockMvc.perform(get("/api/companies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyCapital").value(hasItem(DEFAULT_COMPANY_CAPITAL.intValue())))
            .andExpect(jsonPath("$.[*].companyUniqueIdentification").value(hasItem(DEFAULT_COMPANY_UNIQUE_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].companyRepresentativeFullName").value(hasItem(DEFAULT_COMPANY_REPRESENTATIVE_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].companyFullAddress").value(hasItem(DEFAULT_COMPANY_FULL_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())));
    }
    
    @Test
    public void getCompany() throws Exception {
        // Initialize the database
        companyRepository.save(company);

        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.companyCapital").value(DEFAULT_COMPANY_CAPITAL.intValue()))
            .andExpect(jsonPath("$.companyUniqueIdentification").value(DEFAULT_COMPANY_UNIQUE_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.companyRepresentativeFullName").value(DEFAULT_COMPANY_REPRESENTATIVE_FULL_NAME.toString()))
            .andExpect(jsonPath("$.companyFullAddress").value(DEFAULT_COMPANY_FULL_ADDRESS.toString()))
            .andExpect(jsonPath("$.companyType").value(DEFAULT_COMPANY_TYPE.toString()));
    }

    @Test
    public void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get("/api/companies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateCompany() throws Exception {
        // Initialize the database
        companyRepository.save(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        updatedCompany
            .companyName(UPDATED_COMPANY_NAME)
            .companyCapital(UPDATED_COMPANY_CAPITAL)
            .companyUniqueIdentification(UPDATED_COMPANY_UNIQUE_IDENTIFICATION)
            .companyRepresentativeFullName(UPDATED_COMPANY_REPRESENTATIVE_FULL_NAME)
            .companyFullAddress(UPDATED_COMPANY_FULL_ADDRESS)
            .companyType(UPDATED_COMPANY_TYPE);

        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompany)))
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getCompanyCapital()).isEqualTo(UPDATED_COMPANY_CAPITAL);
        assertThat(testCompany.getCompanyUniqueIdentification()).isEqualTo(UPDATED_COMPANY_UNIQUE_IDENTIFICATION);
        assertThat(testCompany.getCompanyRepresentativeFullName()).isEqualTo(UPDATED_COMPANY_REPRESENTATIVE_FULL_NAME);
        assertThat(testCompany.getCompanyFullAddress()).isEqualTo(UPDATED_COMPANY_FULL_ADDRESS);
        assertThat(testCompany.getCompanyType()).isEqualTo(UPDATED_COMPANY_TYPE);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).save(testCompany);
    }

    @Test
    public void updateNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Create the Company

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc.perform(put("/api/companies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(0)).save(company);
    }

    @Test
    public void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.save(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc.perform(delete("/api/companies/{id}", company.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Company in Elasticsearch
        verify(mockCompanySearchRepository, times(1)).deleteById(company.getId());
    }

    @Test
    public void searchCompany() throws Exception {
        // Initialize the database
        companyRepository.save(company);
        when(mockCompanySearchRepository.search(queryStringQuery("id:" + company.getId())))
            .thenReturn(Collections.singletonList(company));
        // Search the company
        restCompanyMockMvc.perform(get("/api/_search/companies?query=id:" + company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].companyCapital").value(hasItem(DEFAULT_COMPANY_CAPITAL.intValue())))
            .andExpect(jsonPath("$.[*].companyUniqueIdentification").value(hasItem(DEFAULT_COMPANY_UNIQUE_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].companyRepresentativeFullName").value(hasItem(DEFAULT_COMPANY_REPRESENTATIVE_FULL_NAME)))
            .andExpect(jsonPath("$.[*].companyFullAddress").value(hasItem(DEFAULT_COMPANY_FULL_ADDRESS)))
            .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = new Company();
        company1.setId("id1");
        Company company2 = new Company();
        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);
        company2.setId("id2");
        assertThat(company1).isNotEqualTo(company2);
        company1.setId(null);
        assertThat(company1).isNotEqualTo(company2);
    }
}
