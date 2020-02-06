package com.apeiron.paperlabs.repository.search;
import com.apeiron.paperlabs.domain.LegalDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link LegalDocument} entity.
 */
public interface LegalDocumentSearchRepository extends ElasticsearchRepository<LegalDocument, String> {
}
