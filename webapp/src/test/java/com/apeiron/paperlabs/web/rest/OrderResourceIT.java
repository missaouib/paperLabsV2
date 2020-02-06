package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.PaperlabsApp;
import com.apeiron.paperlabs.domain.Order;
import com.apeiron.paperlabs.repository.OrderRepository;
import com.apeiron.paperlabs.repository.search.OrderSearchRepository;
import com.apeiron.paperlabs.service.OrderService;
import com.apeiron.paperlabs.service.UserService;
import com.apeiron.paperlabs.service.dto.OrderDTO;
import com.apeiron.paperlabs.service.mapper.OrderMapper;
import com.apeiron.paperlabs.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.util.Collections;
import java.util.List;

import static com.apeiron.paperlabs.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.apeiron.paperlabs.domain.enumeration.PaymentMethod;
import com.apeiron.paperlabs.domain.enumeration.OrderStatus;
/**
 * Integration tests for the {@link OrderResource} REST controller.
 */
@SpringBootTest(classes = PaperlabsApp.class)
public class OrderResourceIT {

    private static final PaymentMethod DEFAULT_PAYMENT_METHOD = PaymentMethod.MOBICACH;
    private static final PaymentMethod UPDATED_PAYMENT_METHOD = PaymentMethod.EDINAR;

    private static final Float DEFAULT_TOTAL_PRICE = 1F;
    private static final Float UPDATED_TOTAL_PRICE = 2F;
    private static final Float SMALLER_TOTAL_PRICE = 1F - 1F;

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;
    private static final Float SMALLER_PRICE = 1F - 1F;

    private static final Float DEFAULT_TAX = 1F;
    private static final Float UPDATED_TAX = 2F;
    private static final Float SMALLER_TAX = 1F - 1F;

    private static final String DEFAULT_INVOICE_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_FILE_PATH = "BBBBBBBBBB";

    private static final OrderStatus DEFAULT_STATUS = OrderStatus.WAITING;
    private static final OrderStatus UPDATED_STATUS = OrderStatus.PAID;

    private static final String DEFAULT_DESTINATION_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_EMAIL = "BBBBBBBBBB";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * This repository is mocked in the com.apeiron.paperlabs.repository.search test package.
     *
     * @see com.apeiron.paperlabs.repository.search.OrderSearchRepositoryMockConfiguration
     */
    @Autowired
    private OrderSearchRepository mockOrderSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restOrderMockMvc;

    private Order order;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrderResource orderResource = new OrderResource(orderService, userService);
        this.restOrderMockMvc = MockMvcBuilders.standaloneSetup(orderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity() {
        Order order = new Order()
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .price(DEFAULT_PRICE)
            .tax(DEFAULT_TAX)
            .invoiceFilePath(DEFAULT_INVOICE_FILE_PATH)
            .status(DEFAULT_STATUS)
            .destinationEmail(DEFAULT_DESTINATION_EMAIL);
        return order;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity() {
        Order order = new Order()
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .price(UPDATED_PRICE)
            .tax(UPDATED_TAX)
            .invoiceFilePath(UPDATED_INVOICE_FILE_PATH)
            .status(UPDATED_STATUS)
            .destinationEmail(UPDATED_DESTINATION_EMAIL);
        return order;
    }

    @BeforeEach
    public void initTest() {
        orderRepository.deleteAll();
        order = createEntity();
    }

    @Test
    public void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
        assertThat(testOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testOrder.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrder.getTax()).isEqualTo(DEFAULT_TAX);
        assertThat(testOrder.getInvoiceFilePath()).isEqualTo(DEFAULT_INVOICE_FILE_PATH);
        assertThat(testOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOrder.getDestinationEmail()).isEqualTo(DEFAULT_DESTINATION_EMAIL);

        // Validate the Order in Elasticsearch
        verify(mockOrderSearchRepository, times(1)).save(testOrder);
    }

    @Test
    public void createOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // Create the Order with an existing ID
        order.setId("existing_id");
        OrderDTO orderDTO = orderMapper.toDto(order);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);

        // Validate the Order in Elasticsearch
        verify(mockOrderSearchRepository, times(0)).save(order);
    }


    @Test
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setTotalPrice(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setPrice(null);

        // Create the Order, which fails.
        OrderDTO orderDTO = orderMapper.toDto(order);

        restOrderMockMvc.perform(post("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.save(order);

        // Get all the orderList
        restOrderMockMvc.perform(get("/api/orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].invoiceFilePath").value(hasItem(DEFAULT_INVOICE_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].destinationEmail").value(hasItem(DEFAULT_DESTINATION_EMAIL.toString())));
    }
    
    @Test
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order);

        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId()))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD.toString()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.tax").value(DEFAULT_TAX.doubleValue()))
            .andExpect(jsonPath("$.invoiceFilePath").value(DEFAULT_INVOICE_FILE_PATH.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.destinationEmail").value(DEFAULT_DESTINATION_EMAIL.toString()));
    }

    @Test
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        updatedOrder
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .price(UPDATED_PRICE)
            .tax(UPDATED_TAX)
            .invoiceFilePath(UPDATED_INVOICE_FILE_PATH)
            .status(UPDATED_STATUS)
            .destinationEmail(UPDATED_DESTINATION_EMAIL);
        OrderDTO orderDTO = orderMapper.toDto(updatedOrder);

        restOrderMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
        assertThat(testOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testOrder.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrder.getTax()).isEqualTo(UPDATED_TAX);
        assertThat(testOrder.getInvoiceFilePath()).isEqualTo(UPDATED_INVOICE_FILE_PATH);
        assertThat(testOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOrder.getDestinationEmail()).isEqualTo(UPDATED_DESTINATION_EMAIL);

        // Validate the Order in Elasticsearch
        verify(mockOrderSearchRepository, times(1)).save(testOrder);
    }

    @Test
    public void updateNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Create the Order
        OrderDTO orderDTO = orderMapper.toDto(order);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc.perform(put("/api/orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(orderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Order in Elasticsearch
        verify(mockOrderSearchRepository, times(0)).save(order);
    }

    @Test
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc.perform(delete("/api/orders/{id}", order.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Order in Elasticsearch
        verify(mockOrderSearchRepository, times(1)).deleteById(order.getId());
    }

    @Test
    public void searchOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order);
        when(mockOrderSearchRepository.search(queryStringQuery("id:" + order.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(order), PageRequest.of(0, 1), 1));
        // Search the order
        restOrderMockMvc.perform(get("/api/_search/orders?query=id:" + order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId())))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].tax").value(hasItem(DEFAULT_TAX.doubleValue())))
            .andExpect(jsonPath("$.[*].invoiceFilePath").value(hasItem(DEFAULT_INVOICE_FILE_PATH)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].destinationEmail").value(hasItem(DEFAULT_DESTINATION_EMAIL)));
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Order.class);
        Order order1 = new Order();
        order1.setId("id1");
        Order order2 = new Order();
        order2.setId(order1.getId());
        assertThat(order1).isEqualTo(order2);
        order2.setId("id2");
        assertThat(order1).isNotEqualTo(order2);
        order1.setId(null);
        assertThat(order1).isNotEqualTo(order2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderDTO.class);
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId("id1");
        OrderDTO orderDTO2 = new OrderDTO();
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
        orderDTO2.setId(orderDTO1.getId());
        assertThat(orderDTO1).isEqualTo(orderDTO2);
        orderDTO2.setId("id2");
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
        orderDTO1.setId(null);
        assertThat(orderDTO1).isNotEqualTo(orderDTO2);
    }
}
