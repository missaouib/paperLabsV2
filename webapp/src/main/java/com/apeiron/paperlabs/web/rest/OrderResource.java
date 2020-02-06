package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.domain.User;
import com.apeiron.paperlabs.service.OrderService;
import com.apeiron.paperlabs.service.UserService;
import com.apeiron.paperlabs.service.dto.OrderDTO;
import com.apeiron.paperlabs.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.apeiron.paperlabs.domain.Order}.
 */
@RestController
@RequestMapping("/api")
public class OrderResource {

    private static final String ENTITY_NAME = "order";
    private final Logger log = LoggerFactory.getLogger(OrderResource.class);
    private final OrderService orderService;
    private final UserService userService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    public OrderResource(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * {@code POST  /orders} : Create a new order.
     *
     * @param orderDTO the orderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderDTO, or with status {@code 400 (Bad Request)} if the order has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) throws URISyntaxException {
        log.debug("REST request to save Order : {}", orderDTO);
        if (orderDTO.getId() != null) {
            throw new BadRequestAlertException("A new order cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderDTO result = orderService.save(orderDTO);
        return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /orders} : Updates an existing order.
     *
     * @param orderDTO the orderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderDTO,
     * or with status {@code 400 (Bad Request)} if the orderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orders")
    public ResponseEntity<OrderDTO> updateOrder(@Valid @RequestBody OrderDTO orderDTO) throws URISyntaxException {
        log.debug("REST request to update Order : {}", orderDTO);
        if (orderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderDTO result = orderService.save(orderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderDTO.getId()))
            .body(result);
    }

    /**
     * {@code GET  /orders} : get all the orders.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(Pageable pageable) {
        log.debug("REST request to get a page of Orders");
        Page<OrderDTO> page = orderService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orders/:id} : get the "id" order.
     *
     * @param id the id of the orderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String id) {
        log.debug("REST request to get Order : {}", id);
        Optional<OrderDTO> orderDTO = orderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderDTO);
    }

    /**
     * {@code DELETE  /orders/:id} : delete the "id" order.
     *
     * @param id the id of the orderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        log.debug("REST request to delete Order : {}", id);
        orderService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }

    /**
     * {@code SEARCH  /_search/orders?query=:query} : search for the order corresponding
     * to the query.
     *
     * @param query    the query of the order search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/orders")
    public ResponseEntity<List<OrderDTO>> searchOrders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Orders for query {}", query);
        Page<OrderDTO> page = orderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orders/user/{userId}} : get all the orders by UserId.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orders in body.
     */
    @GetMapping("/orders/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByUserId(@PathVariable String userId, @RequestParam int pageNumber, @RequestParam int itemsPerPage) {
        log.debug("REST request to get a page of Orders by userID");
        Pageable pageable = PageRequest.of(pageNumber, itemsPerPage);
        Page<OrderDTO> page = orderService.getAllOrdersByUserId(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orders/:orderId} : get the "orderId" order.
     *
     * @param orderId the id of the orderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders/authenticatedUser/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByAuthenticatedUser(@PathVariable String orderId) {
        log.debug("REST request to get Order by authenticated user with orderId : {}", orderId);
        Optional<User> user = userService.getUserWithAuthorities();
        Optional<OrderDTO> orderDTO = Optional.empty();
        if (user.isPresent()) {
            orderDTO = orderService.findOneByUserId(orderId, user.get().getLogin());
        }
        return ResponseUtil.wrapOrNotFound(orderDTO);
    }

}
