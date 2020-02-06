package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.service.dto.LawyerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.apeiron.paperlabs.domain.Lawyer}.
 */
public interface LawyerService {

    /**
     * Save a lawyer.
     *
     * @param lawyerDTO the entity to save.
     * @return the persisted entity.
     */
    LawyerDTO save(LawyerDTO lawyerDTO);

    /**
     * Get all the lawyers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LawyerDTO> findAll(Pageable pageable);


    /**
     * Get the "id" lawyer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LawyerDTO> findOne(String id);

    /**
     * Delete the "id" lawyer.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the lawyer corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LawyerDTO> search(String query, Pageable pageable);
}
