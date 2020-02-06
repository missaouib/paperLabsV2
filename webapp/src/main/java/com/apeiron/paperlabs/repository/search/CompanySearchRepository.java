package com.apeiron.paperlabs.repository.search;
import com.apeiron.paperlabs.domain.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Company} entity.
 */
public interface CompanySearchRepository extends ElasticsearchRepository<Company, String> {
}
