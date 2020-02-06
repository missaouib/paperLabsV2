package com.apeiron.paperlabs.service;

import com.apeiron.paperlabs.service.dto.InvoiceData;
import com.apeiron.paperlabs.service.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.apeiron.paperlabs.domain.Order}.
 */
public interface OrderService {

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    OrderDTO save(OrderDTO orderDTO);

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderDTO> findAll(Pageable pageable);


    /**
     * Get the "id" order.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderDTO> findOne(String id);

    /**
     * Delete the "id" order.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * Search for the order corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderDTO> search(String query, Pageable pageable);

    /**
     * get all Orders by userId.
     *
     * @param userId the UserId.
     *
     * @param pageable the pagination information.
     * @return the list of orders.
     */
    Page<OrderDTO> getAllOrdersByUserId(String userId, Pageable pageable);

    /**
     * Get the "orderId" order.
     *
     * @param orderId the id of the entity.
     * @param userId the id of the authenticated user.
     * @return the entity.
     */
    Optional<OrderDTO> findOneByUserId(String orderId, String userId);

    void convertDocxToPdf(String orderId);
    /**
     * fill the invoice to be printed from the order
     * @param orderId th order to be printed id.
     * @return InvoiceData.
     */
    InvoiceData fillInvoiceData(String orderId);
}
