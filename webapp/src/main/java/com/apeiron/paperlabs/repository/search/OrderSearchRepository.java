package com.apeiron.paperlabs.repository.search;
import com.apeiron.paperlabs.domain.Order;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Order} entity.
 */
public interface OrderSearchRepository extends ElasticsearchRepository<Order, String> {
}
