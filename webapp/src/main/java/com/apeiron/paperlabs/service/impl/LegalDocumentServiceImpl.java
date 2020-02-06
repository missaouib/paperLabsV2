package com.apeiron.paperlabs.service.impl;

import com.apeiron.paperlabs.domain.LegalDocument;
import com.apeiron.paperlabs.domain.Order;
import com.apeiron.paperlabs.domain.enumeration.OrderStatus;
import com.apeiron.paperlabs.repository.LegalDocumentRepository;
import com.apeiron.paperlabs.repository.OrderRepository;
import com.apeiron.paperlabs.repository.search.LegalDocumentSearchRepository;
import com.apeiron.paperlabs.service.LegalDocumentService;
import com.apeiron.paperlabs.service.dto.LegalDocumentDTO;
import com.apeiron.paperlabs.service.mapper.LegalDocumentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link LegalDocument}.
 */
@Service
public class LegalDocumentServiceImpl implements LegalDocumentService {

    private final Logger log = LoggerFactory.getLogger(LegalDocumentServiceImpl.class);

    private final LegalDocumentRepository legalDocumentRepository;

    private final OrderRepository orderRepository;

    private final LegalDocumentMapper legalDocumentMapper;

    private final LegalDocumentSearchRepository legalDocumentSearchRepository;

    private final MongoTemplate mongoTemplate;



    public LegalDocumentServiceImpl(
        LegalDocumentRepository legalDocumentRepository,
        OrderRepository orderRepository,
        LegalDocumentMapper legalDocumentMapper,
        LegalDocumentSearchRepository legalDocumentSearchRepository,
        MongoTemplate mongoTemplate) {
        this.legalDocumentRepository = legalDocumentRepository;
        this.orderRepository = orderRepository;
        this.legalDocumentMapper = legalDocumentMapper;
        this.legalDocumentSearchRepository = legalDocumentSearchRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Save a legalDocument.
     *
     * @param legalDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LegalDocumentDTO save(LegalDocumentDTO legalDocumentDTO) {
        log.debug("Request to save LegalDocument : {}", legalDocumentDTO);
        LegalDocument legalDocument = legalDocumentMapper.toEntity(legalDocumentDTO);
        legalDocument = legalDocumentRepository.save(legalDocument);
        LegalDocumentDTO result = legalDocumentMapper.toDto(legalDocument);
        legalDocumentSearchRepository.save(legalDocument);
        return result;
    }

    /**
     * Get all the legalDocuments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<LegalDocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LegalDocuments");
        return legalDocumentRepository.findAll(pageable)
            .map(legalDocumentMapper::toDto);
    }


    /**
     * Get one legalDocument by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<LegalDocumentDTO> findOne(String id) {
        log.debug("Request to get LegalDocument : {}", id);
        return legalDocumentRepository.findById(id)
            .map(legalDocumentMapper::toDto);
    }

    /**
     * Delete the legalDocument by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete LegalDocument : {}", id);
        legalDocumentRepository.deleteById(id);
        legalDocumentSearchRepository.deleteById(id);
    }

    /**
     * Search for the legalDocument corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<LegalDocumentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LegalDocuments for query {}", query);
        return legalDocumentSearchRepository.search(queryStringQuery(query), pageable)
            .map(legalDocumentMapper::toDto);
    }

    /**
     * Search for the legalDocument full name short name keywords corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    public List<LegalDocumentDTO> searchWithFields(String query) {
        log.debug("Request to search for a page of LegalDocuments for query {}", query);
        Query mongoQuery = new Query();
        Criteria criteria1 = new Criteria().where("shortName").regex(query, "i");
        Criteria criteria2 = new Criteria().where("fullName").regex(query, "i");
        Criteria criteria3 = new Criteria().where("keywords").regex(query, "i");
        mongoQuery.addCriteria(new Criteria().orOperator(criteria1,criteria2,criteria3));
        List<LegalDocument> documents = mongoTemplate.find(mongoQuery, LegalDocument.class);

        return legalDocumentMapper.toDto(documents);

    }

    @Override
    public LinkedList<LegalDocumentDTO> findPopularLegalDocuments() {

        List<LegalDocumentDTO> legalDocuments = legalDocumentMapper.toDto(legalDocumentRepository.findAll());
        LinkedHashMap<LegalDocumentDTO, Integer> legalDocumentsWithTimesPurchased = new LinkedHashMap<>();
        for (LegalDocumentDTO legalDocument: legalDocuments) {
            List<Order> orders = orderRepository.findAllByLegalDocumentIdEqualsAndStatusEquals(legalDocument.getId(), OrderStatus.PAID);
            legalDocumentsWithTimesPurchased.put(legalDocument, orders.size());
        }

        Stream<Map.Entry<LegalDocumentDTO, Integer>> sortedStreamLegalDocuments =
            legalDocumentsWithTimesPurchased.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        return new LinkedList<>(sortedStreamLegalDocuments.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
            (oldValue, newValue) -> oldValue, LinkedHashMap::new)).keySet());

    }
}
