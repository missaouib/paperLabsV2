package com.apeiron.paperlabs.repository.search;
import com.apeiron.paperlabs.domain.Employer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Employer} entity.
 */
public interface EmployerSearchRepository extends ElasticsearchRepository<Employer, String> {
}
