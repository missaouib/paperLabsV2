package com.apeiron.paperlabs.repository.search;
import com.apeiron.paperlabs.domain.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Employee} entity.
 */
public interface EmployeeSearchRepository extends ElasticsearchRepository<Employee, String> {
}
