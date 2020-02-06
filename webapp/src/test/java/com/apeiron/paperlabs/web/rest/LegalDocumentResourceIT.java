package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.PaperlabsApp;
import com.apeiron.paperlabs.domain.LegalDocument;
import com.apeiron.paperlabs.repository.LegalDocumentRepository;
import com.apeiron.paperlabs.repository.search.LegalDocumentSearchRepository;
import com.apeiron.paperlabs.service.LegalDocumentService;
import com.apeiron.paperlabs.service.dto.LegalDocumentDTO;
import com.apeiron.paperlabs.service.mapper.LegalDocumentMapper;
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
 * Integration tests for the {@link LegalDocumentResource} REST controller.
 */
@SpringBootTest(classes = PaperlabsApp.class)
public class LegalDocumentResourceIT {

    private static final String DEFAULT_SHORT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORDS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_PREVIEW_PATH = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_PREVIEW_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_STEPPER_CONFIG_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_STEPPER_CONFIG_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_WORKFLOW_CONFIG_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_WORKFLOW_CONFIG_FILE_PATH = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    @Autowired
    private LegalDocumentRepository legalDocumentRepository;

    @Autowired
    private LegalDocumentMapper legalDocumentMapper;

    @Autowired
    private LegalDocumentService legalDocumentService;

    /**
     * This repository is mocked in the com.apeiron.paperlabs.repository.search test package.
     *
     * @see com.apeiron.paperlabs.repository.search.LegalDocumentSearchRepositoryMockConfiguration
     */
    @Autowired
    private LegalDocumentSearchRepository mockLegalDocumentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restLegalDocumentMockMvc;

    private LegalDocument legalDocument;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LegalDocumentResource legalDocumentResource = new LegalDocumentResource(legalDocumentService);
        this.restLegalDocumentMockMvc = MockMvcBuilders.standaloneSetup(legalDocumentResource)
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
    public static LegalDocument createEntity() {
        LegalDocument legalDocument = new LegalDocument()
            .shortName(DEFAULT_SHORT_NAME)
            .fullName(DEFAULT_FULL_NAME)
            .keywords(DEFAULT_KEYWORDS)
            .description(DEFAULT_DESCRIPTION)
            .templatePreviewPath(DEFAULT_TEMPLATE_PREVIEW_PATH)
            .templateFilePath(DEFAULT_TEMPLATE_FILE_PATH)
            .stepperConfigFilePath(DEFAULT_STEPPER_CONFIG_FILE_PATH)
            .workflowConfigFilePath(DEFAULT_WORKFLOW_CONFIG_FILE_PATH)
            .price(DEFAULT_PRICE);
        return legalDocument;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LegalDocument createUpdatedEntity() {
        LegalDocument legalDocument = new LegalDocument()
            .shortName(UPDATED_SHORT_NAME)
            .fullName(UPDATED_FULL_NAME)
            .keywords(UPDATED_KEYWORDS)
            .description(UPDATED_DESCRIPTION)
            .templatePreviewPath(UPDATED_TEMPLATE_PREVIEW_PATH)
            .templateFilePath(UPDATED_TEMPLATE_FILE_PATH)
            .stepperConfigFilePath(UPDATED_STEPPER_CONFIG_FILE_PATH)
            .workflowConfigFilePath(UPDATED_WORKFLOW_CONFIG_FILE_PATH)
            .price(UPDATED_PRICE);
        return legalDocument;
    }

    @BeforeEach
    public void initTest() {
        legalDocumentRepository.deleteAll();
        legalDocument = createEntity();
    }

    @Test
    public void createLegalDocument() throws Exception {
        int databaseSizeBeforeCreate = legalDocumentRepository.findAll().size();

        // Create the LegalDocument
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);
        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isCreated());

        // Validate the LegalDocument in the database
        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        LegalDocument testLegalDocument = legalDocumentList.get(legalDocumentList.size() - 1);
        assertThat(testLegalDocument.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testLegalDocument.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testLegalDocument.getKeywords()).isEqualTo(DEFAULT_KEYWORDS);
        assertThat(testLegalDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLegalDocument.getTemplatePreviewPath()).isEqualTo(DEFAULT_TEMPLATE_PREVIEW_PATH);
        assertThat(testLegalDocument.getTemplateFilePath()).isEqualTo(DEFAULT_TEMPLATE_FILE_PATH);
        assertThat(testLegalDocument.getStepperConfigFilePath()).isEqualTo(DEFAULT_STEPPER_CONFIG_FILE_PATH);
        assertThat(testLegalDocument.getWorkflowConfigFilePath()).isEqualTo(DEFAULT_WORKFLOW_CONFIG_FILE_PATH);
        assertThat(testLegalDocument.getPrice()).isEqualTo(DEFAULT_PRICE);

        // Validate the LegalDocument in Elasticsearch
        verify(mockLegalDocumentSearchRepository, times(1)).save(testLegalDocument);
    }

    @Test
    public void createLegalDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = legalDocumentRepository.findAll().size();

        // Create the LegalDocument with an existing ID
        legalDocument.setId("existing_id");
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LegalDocument in the database
        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeCreate);

        // Validate the LegalDocument in Elasticsearch
        verify(mockLegalDocumentSearchRepository, times(0)).save(legalDocument);
    }


    @Test
    public void checkShortNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalDocumentRepository.findAll().size();
        // set the field null
        legalDocument.setShortName(null);

        // Create the LegalDocument, which fails.
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalDocumentRepository.findAll().size();
        // set the field null
        legalDocument.setFullName(null);

        // Create the LegalDocument, which fails.
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalDocumentRepository.findAll().size();
        // set the field null
        legalDocument.setDescription(null);

        // Create the LegalDocument, which fails.
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTemplatePreviewPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalDocumentRepository.findAll().size();
        // set the field null
        legalDocument.setTemplatePreviewPath(null);

        // Create the LegalDocument, which fails.
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTemplateFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalDocumentRepository.findAll().size();
        // set the field null
        legalDocument.setTemplateFilePath(null);

        // Create the LegalDocument, which fails.
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkStepperConfigFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalDocumentRepository.findAll().size();
        // set the field null
        legalDocument.setStepperConfigFilePath(null);

        // Create the LegalDocument, which fails.
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkWorkflowConfigFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = legalDocumentRepository.findAll().size();
        // set the field null
        legalDocument.setWorkflowConfigFilePath(null);

        // Create the LegalDocument, which fails.
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        restLegalDocumentMockMvc.perform(post("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllLegalDocuments() throws Exception {
        // Initialize the database
        legalDocumentRepository.save(legalDocument);

        // Get all the legalDocumentList
        restLegalDocumentMockMvc.perform(get("/api/legal-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalDocument.getId())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME.toString())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME.toString())))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].templatePreviewPath").value(hasItem(DEFAULT_TEMPLATE_PREVIEW_PATH.toString())))
            .andExpect(jsonPath("$.[*].templateFilePath").value(hasItem(DEFAULT_TEMPLATE_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].stepperConfigFilePath").value(hasItem(DEFAULT_STEPPER_CONFIG_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].workflowConfigFilePath").value(hasItem(DEFAULT_WORKFLOW_CONFIG_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }
    
    @Test
    public void getLegalDocument() throws Exception {
        // Initialize the database
        legalDocumentRepository.save(legalDocument);

        // Get the legalDocument
        restLegalDocumentMockMvc.perform(get("/api/legal-documents/{id}", legalDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(legalDocument.getId()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME.toString()))
            .andExpect(jsonPath("$.keywords").value(DEFAULT_KEYWORDS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.templatePreviewPath").value(DEFAULT_TEMPLATE_PREVIEW_PATH.toString()))
            .andExpect(jsonPath("$.templateFilePath").value(DEFAULT_TEMPLATE_FILE_PATH.toString()))
            .andExpect(jsonPath("$.stepperConfigFilePath").value(DEFAULT_STEPPER_CONFIG_FILE_PATH.toString()))
            .andExpect(jsonPath("$.workflowConfigFilePath").value(DEFAULT_WORKFLOW_CONFIG_FILE_PATH.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    public void getNonExistingLegalDocument() throws Exception {
        // Get the legalDocument
        restLegalDocumentMockMvc.perform(get("/api/legal-documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateLegalDocument() throws Exception {
        // Initialize the database
        legalDocumentRepository.save(legalDocument);

        int databaseSizeBeforeUpdate = legalDocumentRepository.findAll().size();

        // Update the legalDocument
        LegalDocument updatedLegalDocument = legalDocumentRepository.findById(legalDocument.getId()).get();
        updatedLegalDocument
            .shortName(UPDATED_SHORT_NAME)
            .fullName(UPDATED_FULL_NAME)
            .keywords(UPDATED_KEYWORDS)
            .description(UPDATED_DESCRIPTION)
            .templatePreviewPath(UPDATED_TEMPLATE_PREVIEW_PATH)
            .templateFilePath(UPDATED_TEMPLATE_FILE_PATH)
            .stepperConfigFilePath(UPDATED_STEPPER_CONFIG_FILE_PATH)
            .workflowConfigFilePath(UPDATED_WORKFLOW_CONFIG_FILE_PATH)
            .price(UPDATED_PRICE);
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(updatedLegalDocument);

        restLegalDocumentMockMvc.perform(put("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isOk());

        // Validate the LegalDocument in the database
        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeUpdate);
        LegalDocument testLegalDocument = legalDocumentList.get(legalDocumentList.size() - 1);
        assertThat(testLegalDocument.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testLegalDocument.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testLegalDocument.getKeywords()).isEqualTo(UPDATED_KEYWORDS);
        assertThat(testLegalDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLegalDocument.getTemplatePreviewPath()).isEqualTo(UPDATED_TEMPLATE_PREVIEW_PATH);
        assertThat(testLegalDocument.getTemplateFilePath()).isEqualTo(UPDATED_TEMPLATE_FILE_PATH);
        assertThat(testLegalDocument.getStepperConfigFilePath()).isEqualTo(UPDATED_STEPPER_CONFIG_FILE_PATH);
        assertThat(testLegalDocument.getWorkflowConfigFilePath()).isEqualTo(UPDATED_WORKFLOW_CONFIG_FILE_PATH);
        assertThat(testLegalDocument.getPrice()).isEqualTo(UPDATED_PRICE);

        // Validate the LegalDocument in Elasticsearch
        verify(mockLegalDocumentSearchRepository, times(1)).save(testLegalDocument);
    }

    @Test
    public void updateNonExistingLegalDocument() throws Exception {
        int databaseSizeBeforeUpdate = legalDocumentRepository.findAll().size();

        // Create the LegalDocument
        LegalDocumentDTO legalDocumentDTO = legalDocumentMapper.toDto(legalDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegalDocumentMockMvc.perform(put("/api/legal-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LegalDocument in the database
        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalDocument in Elasticsearch
        verify(mockLegalDocumentSearchRepository, times(0)).save(legalDocument);
    }

    @Test
    public void deleteLegalDocument() throws Exception {
        // Initialize the database
        legalDocumentRepository.save(legalDocument);

        int databaseSizeBeforeDelete = legalDocumentRepository.findAll().size();

        // Delete the legalDocument
        restLegalDocumentMockMvc.perform(delete("/api/legal-documents/{id}", legalDocument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LegalDocument> legalDocumentList = legalDocumentRepository.findAll();
        assertThat(legalDocumentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LegalDocument in Elasticsearch
        verify(mockLegalDocumentSearchRepository, times(1)).deleteById(legalDocument.getId());
    }

    @Test
    public void searchLegalDocument() throws Exception {
        // Initialize the database
        legalDocumentRepository.save(legalDocument);
        when(mockLegalDocumentSearchRepository.search(queryStringQuery("id:" + legalDocument.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(legalDocument), PageRequest.of(0, 1), 1));
        // Search the legalDocument
        restLegalDocumentMockMvc.perform(get("/api/_search/legal-documents?query=id:" + legalDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalDocument.getId())))
            .andExpect(jsonPath("$.[*].shortName").value(hasItem(DEFAULT_SHORT_NAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].keywords").value(hasItem(DEFAULT_KEYWORDS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].templatePreviewPath").value(hasItem(DEFAULT_TEMPLATE_PREVIEW_PATH)))
            .andExpect(jsonPath("$.[*].templateFilePath").value(hasItem(DEFAULT_TEMPLATE_FILE_PATH)))
            .andExpect(jsonPath("$.[*].stepperConfigFilePath").value(hasItem(DEFAULT_STEPPER_CONFIG_FILE_PATH)))
            .andExpect(jsonPath("$.[*].workflowConfigFilePath").value(hasItem(DEFAULT_WORKFLOW_CONFIG_FILE_PATH)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LegalDocument.class);
        LegalDocument legalDocument1 = new LegalDocument();
        legalDocument1.setId("id1");
        LegalDocument legalDocument2 = new LegalDocument();
        legalDocument2.setId(legalDocument1.getId());
        assertThat(legalDocument1).isEqualTo(legalDocument2);
        legalDocument2.setId("id2");
        assertThat(legalDocument1).isNotEqualTo(legalDocument2);
        legalDocument1.setId(null);
        assertThat(legalDocument1).isNotEqualTo(legalDocument2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LegalDocumentDTO.class);
        LegalDocumentDTO legalDocumentDTO1 = new LegalDocumentDTO();
        legalDocumentDTO1.setId("id1");
        LegalDocumentDTO legalDocumentDTO2 = new LegalDocumentDTO();
        assertThat(legalDocumentDTO1).isNotEqualTo(legalDocumentDTO2);
        legalDocumentDTO2.setId(legalDocumentDTO1.getId());
        assertThat(legalDocumentDTO1).isEqualTo(legalDocumentDTO2);
        legalDocumentDTO2.setId("id2");
        assertThat(legalDocumentDTO1).isNotEqualTo(legalDocumentDTO2);
        legalDocumentDTO1.setId(null);
        assertThat(legalDocumentDTO1).isNotEqualTo(legalDocumentDTO2);
    }
}
