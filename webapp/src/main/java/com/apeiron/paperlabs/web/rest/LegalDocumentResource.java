package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.config.ApplicationProperties;
import com.apeiron.paperlabs.service.LegalDocumentService;
import com.apeiron.paperlabs.service.dto.LegalDocumentDTO;
import com.apeiron.paperlabs.web.rest.errors.BadRequestAlertException;
import com.itextpdf.text.log.SysoCounter;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.apeiron.paperlabs.domain.LegalDocument}.
 */
@RestController
@RequestMapping("/api")
public class LegalDocumentResource {

    private final Logger log = LoggerFactory.getLogger(LegalDocumentResource.class);

    private static final String ENTITY_NAME = "legalDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LegalDocumentService legalDocumentService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public LegalDocumentResource(LegalDocumentService legalDocumentService) {
        this.legalDocumentService = legalDocumentService;
    }

    /**
     * {@code POST  /legal-documents} : Create a new legalDocument.
     *
     * @param legalDocumentDTO the legalDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new legalDocumentDTO, or with status {@code 400 (Bad Request)} if the legalDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/legal-documents")
    public ResponseEntity<LegalDocumentDTO> createLegalDocument(@Valid @RequestBody LegalDocumentDTO legalDocumentDTO) throws URISyntaxException {
        log.debug("REST request to save LegalDocument : {}", legalDocumentDTO);
        if (legalDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new legalDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        LegalDocumentDTO result = legalDocumentService.save(legalDocumentDTO);
        return ResponseEntity.created(new URI("/api/legal-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /legal-documents} : Updates an existing legalDocument.
     *
     * @param legalDocumentDTO the legalDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated legalDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the legalDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the legalDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/legal-documents")
    public ResponseEntity<LegalDocumentDTO> updateLegalDocument(@Valid @RequestBody LegalDocumentDTO legalDocumentDTO) throws URISyntaxException {
        log.debug("REST request to update LegalDocument : {}", legalDocumentDTO);
        if (legalDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LegalDocumentDTO result = legalDocumentService.save(legalDocumentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, legalDocumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /legal-documents} : get all the legalDocuments.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of legalDocuments in body.
     */
    @GetMapping("/legal-documents")
    public ResponseEntity<List<LegalDocumentDTO>> getAllLegalDocuments(Pageable pageable) {
        log.debug("REST request to get a page of LegalDocuments");
        Page<LegalDocumentDTO> page = legalDocumentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /popular-legal-documents} : get all the legalDocuments ordered by popularity.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of legalDocuments in body.
     */
    @GetMapping("/popular-legal-documents")
    public ResponseEntity<LinkedList<LegalDocumentDTO>> getPopularLegalDocuments() {
        log.debug("REST request to get a page of LegalDocuments");
        LinkedList<LegalDocumentDTO> list = legalDocumentService.findPopularLegalDocuments();
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /legal-documents/:id} : get the "id" legalDocument.
     *
     * @param id the id of the legalDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the legalDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/legal-documents/{id}")
    public ResponseEntity<LegalDocumentDTO> getLegalDocument(@PathVariable String id) {
        log.debug("REST request to get LegalDocument : {}", id);
        System.out.println("**********************");
        System.out.println("**********************");
        Optional<LegalDocumentDTO> legalDocumentDTO = legalDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(legalDocumentDTO);
    }

    /**
     * {@code DELETE  /legal-documents/:id} : delete the "id" legalDocument.
     *
     * @param id the id of the legalDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/legal-documents/{id}")
    public ResponseEntity<Void> deleteLegalDocument(@PathVariable String id) {
        log.debug("REST request to delete LegalDocument : {}", id);
        legalDocumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/legal-documents?query=:query} : search for the legalDocument corresponding
     * to the query.
     *
     * @param query the query of the legalDocument search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/legal-documents")
    public ResponseEntity<List<LegalDocumentDTO>> searchLegalDocuments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LegalDocuments for query {}", query);
        Page<LegalDocumentDTO> page = legalDocumentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code SEARCH  /_search/legal-documents/fields?query=:query} : search for the legalDocument corresponding
     * to the query.
     *
     * @param query the query of the legalDocument search.
     * @return the result of the search.
     */
    @GetMapping("/_search/legal-documents/fields")
    public List<LegalDocumentDTO> searchLegalDocumentsWithFields(@RequestParam String query) {
        log.debug("REST request to search for a list of LegalDocuments for query {}", query);
        return legalDocumentService.searchWithFields(query);
    }

    /**
     * {@code GET  /legalDocumentTaxStamp} : get the companyTaxStamp.
     *
     * @return the tax stamp value.
     */
    @GetMapping("/legalDocumentTaxStamp")
    public Double getLegalDocumentTaxStamp() {
        log.debug("REST request to get legal document tax stamp.");
        return applicationProperties.getCompanyTaxStamp();
    }
}
