package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.service.dto.LegalDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.apeiron.paperlabs.domain.LegalDocument}.
 */
public interface LegalDocumentService {

    /**
     * Save a legalDocument.
     *
     * @param legalDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    LegalDocumentDTO save(LegalDocumentDTO legalDocumentDTO);

    /**
     * Get all the legalDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LegalDocumentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" legalDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LegalDocumentDTO> findOne(String id);

    /**
     * Delete the "id" legalDocument.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the legalDocument corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LegalDocumentDTO> search(String query, Pageable pageable);
    /**
     * Search for the legalDocument full nae short name keywords corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @return the list of entities.
     */
    List<LegalDocumentDTO> searchWithFields(String query);

    LinkedList<LegalDocumentDTO> findPopularLegalDocuments();
}
