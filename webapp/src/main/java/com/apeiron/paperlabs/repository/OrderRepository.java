package com.apeiron.paperlabs.repository;

import com.apeiron.paperlabs.domain.Order;
import com.apeiron.paperlabs.domain.enumeration.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data MongoDB repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Page<Order> findAllByUserIdEquals(String userId, Pageable pageable);

    Optional<Order> findAllByUserIdEqualsAndIdEquals(String userId, String orderId);

    List<Order> findAllByStatusEqualsAndGeneratedLegalDocument_LastModifiedDateAfter(OrderStatus orderStatus,Instant instant);

    List<Order> findAllByUserIdEqualsAndStatusEquals(String userId,OrderStatus orderStatus);

    List<Order> findAllByLegalDocumentIdEqualsAndStatusEquals(String documentId,OrderStatus orderStatus);

}
