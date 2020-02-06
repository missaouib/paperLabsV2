package com.apeiron.paperlabs.repository.search;
import com.apeiron.paperlabs.domain.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Category} entity.
 */
public interface CategorySearchRepository extends ElasticsearchRepository<Category, String> {
}
