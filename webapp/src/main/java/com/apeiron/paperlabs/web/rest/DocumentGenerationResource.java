package com.apeiron.paperlabs.web.rest;

import com.apeiron.docflow.domain.InputData;
import com.apeiron.paperlabs.domain.enumeration.OrderStatus;
import com.apeiron.paperlabs.facade.DocumentGenerationFacade;
import com.apeiron.paperlabs.service.FileDownloadService;
import com.apeiron.paperlabs.service.InvoiceService;
import com.apeiron.paperlabs.service.MailService;
import com.apeiron.paperlabs.service.OrderService;
import com.apeiron.paperlabs.service.dto.GeneratedLegalDocumentDTO;
import com.apeiron.paperlabs.service.dto.InvoiceData;
import com.apeiron.paperlabs.service.dto.OrderDTO;

import io.jsonwebtoken.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DocumentGenerationResource {
    private final Logger log = LoggerFactory.getLogger(DocumentGenerationResource.class);


    private final DocumentGenerationFacade documentGenerationFacade;
    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final MailService mailService;
    private final FileDownloadService fileDownloadService;

    public DocumentGenerationResource(
        DocumentGenerationFacade documentGenerationFacade,
        OrderService orderService,
        InvoiceService invoiceService,
        MailService mailService,
        FileDownloadService fileDownloadService
    ) {

        this.documentGenerationFacade = documentGenerationFacade;
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.mailService = mailService;
        this.fileDownloadService = fileDownloadService;
    }
    

    /**
     * POST  /generateDocXFile : submit html form data to generate docX file.
     *
     * @param stepperDataDTO represents html stepper form data submitted by user.
     * @param legalDocumentId represents chosen model id.
     */
    @PostMapping("/generateDocXFile")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<OrderDTO> generateDocXFile(@RequestBody Map<String, InputData> stepperDataDTO, @RequestParam String legalDocumentId, @RequestParam String saveDataAuthorization, @RequestParam String userLogin, @RequestParam(required = false) String orderId) {
        boolean saveDataAuthorizationBoolean = Boolean.parseBoolean(saveDataAuthorization);
        return ResponseEntity.ok().body(documentGenerationFacade.generateDocXFile(stepperDataDTO, legalDocumentId, saveDataAuthorizationBoolean, userLogin, orderId));
    }

    @GetMapping("/currentUserHasConcernedEntityObjects")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Boolean> currentUserHasConcernedEntityObjects(@RequestParam String legalDocumentId, @RequestParam String userLogin) {
        return ResponseEntity.ok().body(documentGenerationFacade.currentUserHasConcernedEntityObjects(legalDocumentId, userLogin));
    }
    
    /**
     * 
     * 
     * 
     */
    
    
    
  
    
    

    /**
     * {@code GET /report/generate-sendPdfFile/{locale}} : generate and send The document Pdf File.
     *
     * @param orderId            The order id
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} .
     */
    @GetMapping(value = "/report/generate-sendPdfFile/{orderId}")
    public ResponseEntity<OrderDTO> generateAndSendThePdfDocument(@PathVariable String orderId) {
        log.debug("REST request to generate and send the pdf document : {}", orderId);
        orderService.convertDocxToPdf(orderId);
        Optional<OrderDTO> optionalOrderDTO = orderService.findOne(orderId);

        try {
            if (optionalOrderDTO.isPresent()) {
                OrderDTO orderDTO = optionalOrderDTO.get();
                orderDTO.setStatus(OrderStatus.PAID);
                GeneratedLegalDocumentDTO generatedLegalDocumentDTO = new GeneratedLegalDocumentDTO();
                generatedLegalDocumentDTO = orderDTO.getGeneratedLegalDocument();
                generatedLegalDocumentDTO.setPaymentDate(Instant.now());
                orderDTO.setGeneratedLegalDocument(generatedLegalDocumentDTO);
                orderService.save(orderDTO);
                mailService.sendOrderDocumentEmail(orderDTO);
                log.info("Sending E-mail with Pdf Document to : {}", orderDTO.getUser().getEmail());
                return ResponseEntity.ok().body(orderDTO);
            }

        } catch (Exception e) {
            log.error("Error Sending Pdf File !!!", e);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    /**
     * {@code GET /report/generate-sendInvoiceFile/{locale}} : generate and send The Invoice Pdf File.
     *
     * @param orderId            The local language for the pdf FR/EN
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} .
     */
    @GetMapping(value = "/report/generate-sendInvoiceFile/{orderId}")
    @Async
    public ResponseEntity<OrderDTO> generateAndSendTheInvoice(@PathVariable String orderId) {
        log.debug("REST request to generate and send the invoice : {}", orderId);
        InvoiceData invoiceData = orderService.fillInvoiceData(orderId);

        Optional<OrderDTO> optionalOrderDTO = orderService.findOne(orderId);
        try {
            if (optionalOrderDTO.isPresent()) {
                OrderDTO orderDTO = optionalOrderDTO.get();
                String filePath = invoiceService.printReport(invoiceData, Locale.FRANCE);
                orderDTO.setInvoiceFilePath(filePath);
                orderService.save(orderDTO);
                mailService.sendOrderInvoiceEmail(orderDTO);
                log.info("Sending E-mail with invoice to : {}", orderDTO.getUser().getEmail());
                return ResponseEntity.ok().body(orderDTO);
            }

        } catch (Exception e) {
            log.error("Error Creating Pdf File !!!", e);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);

    }

    /**
     * {@code GET  /downloadPdfFile} : Downloads a created legal document as a PDF file.
     *
     * @param filePath a request parameter containing the full path of the requested PDF file to download.
     * @param response HttpResponse to set contentType.
     * @return the requested PDF file as a {@link Resource}.
     * @throws UnsupportedEncodingException if the {@param filePath} is not encoded in UTF-8.
     */
    @GetMapping(value = "/downloadPdfFile", produces = "application/pdf; charset=utf-8")
    @ResponseStatus(HttpStatus.OK)
    public Resource downloadPdfFile(@RequestParam String filePath, @RequestParam String orderId, HttpServletResponse response) throws UnsupportedEncodingException {
        return fileDownloadService.downloadPdfFile(URLDecoder.decode(filePath, "UTF-8"), orderId, response);
    }
    
    
   
 
}
