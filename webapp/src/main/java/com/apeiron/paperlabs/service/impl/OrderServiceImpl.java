package com.apeiron.paperlabs.service.impl;

import com.apeiron.paperlabs.config.ApplicationProperties;
import com.apeiron.paperlabs.domain.Order;
import com.apeiron.paperlabs.fileUtils.DocxUtils;
import com.apeiron.paperlabs.repository.OrderRepository;
import com.apeiron.paperlabs.repository.search.OrderSearchRepository;
import com.apeiron.paperlabs.service.NextSequenceService;
import com.apeiron.paperlabs.service.OrderService;
import com.apeiron.paperlabs.service.dto.InvoiceData;
import com.apeiron.paperlabs.service.dto.LegalDocumentDTO;
import com.apeiron.paperlabs.service.dto.OrderDTO;
import com.apeiron.paperlabs.service.mapper.OrderMapper;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final String FILLED_FILE_NAME_PREFIX = "webapp/webapp/generated-pdf/";
    private static final String INDEX_EXTENSION = ".pdf";
    private static final String REGEX = "[^a-zA-Z0-9\\.\\-]";
    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderSearchRepository orderSearchRepository;

    private final ApplicationProperties applicationProperties;

    private final NextSequenceService nextSequenceService;

    private final DocxUtils docxUtils;

    private final MongoTemplate mongoTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, OrderSearchRepository orderSearchRepository, ApplicationProperties applicationProperties, NextSequenceService nextSequenceService, DocxUtils bookmarksDeleter, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderSearchRepository = orderSearchRepository;
        this.applicationProperties = applicationProperties;
        this.nextSequenceService = nextSequenceService;
        this.docxUtils = bookmarksDeleter;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        if (orderDTO.getId() == null) {
            orderDTO.setOrderIdentifier(nextSequenceService.getNextOrderIdentifierSequence());
        }
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        OrderDTO result = orderMapper.toDto(order);
        orderSearchRepository.save(order);
        return result;
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<OrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAll(pageable)
            .map(orderMapper::toDto);
    }


    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<OrderDTO> findOne(String id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id)
            .map(orderMapper::toDto);
    }

    /**
     * Get the "orderId" order.
     *
     * @param orderId the id of the entity.
     * @param userId  the id of the authenticated user.
     * @return the entity.
     */
    @Override
    public Optional<OrderDTO> findOneByUserId(String orderId, String userId) {
        log.debug("Request to get Order : {}, with User : {}", orderId, userId);
        return orderRepository.findAllByUserIdEqualsAndIdEquals(userId, orderId)
            .map(orderMapper::toDto);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
        orderSearchRepository.deleteById(id);
    }

    /**
     * Search for the order corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<OrderDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Orders for query {}", query);

        List<OrderDTO> orderDTOList = searchWithFields(query);
        return new PageImpl<>(orderDTOList);
    }

    private List<OrderDTO> searchWithFields(String query) {
        log.debug("Request to search for a page of orderMapper for the query {}", query);
        Query mongoQuery = new Query();

        if (StringUtils.isNumeric(query)) {
            Criteria criteria1 = new Criteria().where("totalPrice").lte(Double.valueOf(query));
            Criteria criteria2 = new Criteria().where("price").lte(Double.valueOf(query));
            Criteria criteria6 = new Criteria().where("orderIdentifier").is(Integer.valueOf(query));

            mongoQuery.addCriteria(new Criteria().orOperator(criteria1, criteria2,criteria6));
        } else {
            Criteria criteria3 = new Criteria().where("paymentMethod").regex(query, "i");
            Criteria criteria4 = new Criteria().where("status").regex(query, "i");
            Criteria criteria5 = new Criteria().where("destinationEmail").regex(query, "i");

            mongoQuery.addCriteria(new Criteria().orOperator(criteria3, criteria4, criteria5));
        }

        List<Order> documents = mongoTemplate.find(mongoQuery, Order.class);

        return orderMapper.toDto(documents);

    }

    /**
     * get all Orders by userId.
     *
     * @param userId   The UserId corresponding to the order.userId
     * @param pageable the pagination information.
     * @return the list of orders.
     */
    @Override
    public Page<OrderDTO> getAllOrdersByUserId(String userId, Pageable pageable) {
        log.debug("Request to search for a page of Orders for user Id {}", userId);
        return orderRepository.findAllByUserIdEquals(userId, pageable).map(orderMapper::toDto);
    }

    /**
     * Method for converting the docx document to Pdf and update the order by the new pdf file path
     *
     * @param orderId the order id to be updated
     */
    @Override
    public void convertDocxToPdf(String orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            OrderDTO orderDTO = orderMapper.toDto(order.get());
            String docXFilePath = orderDTO.getGeneratedLegalDocument().getGeneratedWordFilePath();
            String documentName = orderDTO.getLegalDocument().getShortName();
            String pdfFilePath = docxToPDF(docXFilePath, documentName);
            orderDTO.getGeneratedLegalDocument().setGenratedPDFFilePath(pdfFilePath);
            this.save(orderDTO);
        }
    }

    /**
     * Method for converting the docx document to Pdf
     *
     * @param docPath      the path of the docx document
     * @param documentName the name of the document
     * @return the pdf file path
     */
    private String docxToPDF(String docPath, String documentName) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());

        String originalName = (timeStamp + documentName).replaceAll(REGEX, "_");
        OutputStream output = null;

        File dirPdfFile = new File(FILLED_FILE_NAME_PREFIX + originalName + INDEX_EXTENSION);
        if (!dirPdfFile.exists()) {
            dirPdfFile.getParentFile().mkdir();
        }
        docxUtils.deleteBookmarks(docPath);
        try (InputStream doc = new FileInputStream(new File(docPath))) {
            try (XWPFDocument document = new XWPFDocument(doc)) {
                PdfOptions options = PdfOptions.create();
                output = new FileOutputStream(dirPdfFile);
                PdfConverter.getInstance().convert(document, output, options);
                docxUtils.encryptPdf(dirPdfFile);
            }
        } catch (IOException e) {

            log.error("Error converting docx to PDF", e);
        }
        if (dirPdfFile.exists() && !dirPdfFile.isDirectory()) {

            return FILLED_FILE_NAME_PREFIX + originalName + INDEX_EXTENSION;
        } else {
            return null;
        }
    }


    /**
     * method for fill the order Data to the InvoiceDate entity for the jasperReport data
     *
     * @param orderId th order to be printed id.
     * @return the InvoiceDate
     */
    public InvoiceData fillInvoiceData(String orderId) {
        InvoiceData invoiceData = new InvoiceData();
        List<LegalDocumentDTO> documents = new ArrayList<>();

        Optional<OrderDTO> orderDTO = orderRepository.findById(orderId).map(orderMapper::toDto);

        invoiceData.setCompanyFullAddres(applicationProperties.getCompanyAddress());
        invoiceData.setTva(applicationProperties.getCompanyInvoiceTva());
        invoiceData.setTaxStamp(applicationProperties.getCompanyTaxStamp());
        invoiceData.setCompanyMF(applicationProperties.getCompanyMF());
        invoiceData.setCompanyRC(applicationProperties.getCompanyRC());
        invoiceData.setFooterLine1(applicationProperties.getCompanyInvoiceFooterLine1());
        invoiceData.setFooterLine2(applicationProperties.getCompanyInvoiceFooterLine2());

        if (orderDTO.isPresent()) {
            OrderDTO order = orderDTO.get();
            documents.add(order.getLegalDocument());
            invoiceData.setReciverFullName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
            invoiceData.setPaymentMethod(order.getPaymentMethod());
            if (order.getUser().getAddress() != null) {
                invoiceData.setReciverAddressLine1(order.getUser().getAddress().getLine1());
                invoiceData.setReciverAddressLine2(order.getUser().getAddress().getLine2());
            }
        }
        invoiceData.setDocuments(documents);
        return invoiceData;
    }
}
