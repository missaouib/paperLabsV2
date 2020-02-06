package com.apeiron.paperlabs.repository.search;
import com.apeiron.paperlabs.domain.Lawyer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Lawyer} entity.
 */
public interface LawyerSearchRepository extends ElasticsearchRepository<Lawyer, String> {
}
