package com.apeiron.paperlabs.service.impl;

import com.apeiron.paperlabs.service.LawyerService;
import com.apeiron.paperlabs.domain.Lawyer;
import com.apeiron.paperlabs.repository.LawyerRepository;
import com.apeiron.paperlabs.repository.search.LawyerSearchRepository;
import com.apeiron.paperlabs.service.dto.LawyerDTO;
import com.apeiron.paperlabs.service.mapper.LawyerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Lawyer}.
 */
@Service
public class LawyerServiceImpl implements LawyerService {

    private final Logger log = LoggerFactory.getLogger(LawyerServiceImpl.class);

    private final LawyerRepository lawyerRepository;

    private final LawyerMapper lawyerMapper;

    private final LawyerSearchRepository lawyerSearchRepository;

    public LawyerServiceImpl(LawyerRepository lawyerRepository, LawyerMapper lawyerMapper, LawyerSearchRepository lawyerSearchRepository) {
        this.lawyerRepository = lawyerRepository;
        this.lawyerMapper = lawyerMapper;
        this.lawyerSearchRepository = lawyerSearchRepository;
    }

    /**
     * Save a lawyer.
     *
     * @param lawyerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LawyerDTO save(LawyerDTO lawyerDTO) {
        log.debug("Request to save Lawyer : {}", lawyerDTO);
        Lawyer lawyer = lawyerMapper.toEntity(lawyerDTO);
        lawyer = lawyerRepository.save(lawyer);
        LawyerDTO result = lawyerMapper.toDto(lawyer);
        lawyerSearchRepository.save(lawyer);
        return result;
    }

    /**
     * Get all the lawyers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<LawyerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lawyers");
        return lawyerRepository.findAll(pageable)
            .map(lawyerMapper::toDto);
    }


    /**
     * Get one lawyer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<LawyerDTO> findOne(String id) {
        log.debug("Request to get Lawyer : {}", id);
        return lawyerRepository.findById(id)
            .map(lawyerMapper::toDto);
    }

    /**
     * Delete the lawyer by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Lawyer : {}", id);
        lawyerRepository.deleteById(id);
        lawyerSearchRepository.deleteById(id);
    }

    /**
     * Search for the lawyer corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<LawyerDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Lawyers for query {}", query);
        return lawyerSearchRepository.search(queryStringQuery(query), pageable)
            .map(lawyerMapper::toDto);
    }
}
