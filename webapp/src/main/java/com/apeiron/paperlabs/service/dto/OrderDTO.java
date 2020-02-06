package com.apeiron.paperlabs.service.dto;

import com.apeiron.paperlabs.domain.enumeration.OrderStatus;
import com.apeiron.paperlabs.domain.enumeration.PaymentMethod;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.apeiron.paperlabs.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private String id;

    private PaymentMethod paymentMethod;

    @NotNull
    private Float totalPrice;

    @NotNull
    private Float price;

    private Float tax;

    private String invoiceFilePath;

    private OrderStatus status;

    private String destinationEmail;

    private LegalDocumentDTO legalDocument;

    private List<DowloadHistoryDTO> dowloadHistories;

    private GeneratedLegalDocumentDTO generatedLegalDocument;

    private UserDTO user;

    private String orderIdentifier;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTax() {
        return tax;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public String getInvoiceFilePath() {
        return invoiceFilePath;
    }

    public void setInvoiceFilePath(String invoiceFilePath) {
        this.invoiceFilePath = invoiceFilePath;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDestinationEmail() {
        return destinationEmail;
    }

    public void setDestinationEmail(String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }

    public LegalDocumentDTO getLegalDocument() {
        return legalDocument;
    }

    public void setLegalDocument(LegalDocumentDTO legalDocument) {
        this.legalDocument = legalDocument;
    }

    public List<DowloadHistoryDTO> getDowloadHistories() {
        return dowloadHistories;
    }

    public void setDowloadHistories(List<DowloadHistoryDTO> dowloadHistories) {
        this.dowloadHistories = dowloadHistories;
    }

    public GeneratedLegalDocumentDTO getGeneratedLegalDocument() {
        return generatedLegalDocument;
    }

    public void setGeneratedLegalDocument(GeneratedLegalDocumentDTO generatedLegalDocument) {
        this.generatedLegalDocument = generatedLegalDocument;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getOrderIdentifier() {
        return orderIdentifier;
    }

    public void setOrderIdentifier(String orderIdentifier) {
        this.orderIdentifier = orderIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (orderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id='" + id + '\'' +
            ", paymentMethod=" + paymentMethod +
            ", totalPrice=" + totalPrice +
            ", price=" + price +
            ", tax=" + tax +
            ", invoiceFilePath='" + invoiceFilePath + '\'' +
            ", status=" + status +
            ", destinationEmail='" + destinationEmail + '\'' +
            ", legalDocument=" + legalDocument +
            ", dowloadHistories=" + dowloadHistories +
            ", generatedLegalDocument=" + generatedLegalDocument +
            ", user=" + user +
            ", OrderIdentifier=" + orderIdentifier +
            '}';
    }
}
