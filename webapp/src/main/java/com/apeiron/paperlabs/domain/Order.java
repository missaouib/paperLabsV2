package com.apeiron.paperlabs.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.List;

import com.apeiron.paperlabs.domain.enumeration.PaymentMethod;

import com.apeiron.paperlabs.domain.enumeration.OrderStatus;

/**
 * A Order.
 */
@Document(collection = "order")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("payment_method")
    private PaymentMethod paymentMethod;

    @NotNull
    @Field("total_price")
    private Float totalPrice;

    @NotNull
    @Field("price")
    private Float price;

    @Field("tax")
    private Float tax;

    @Field("invoice_file_path")
    private String invoiceFilePath;

    @Field("status")
    private OrderStatus status;

    @Field("destination_email")
    private String destinationEmail;

    @Field("legal_document_id")
    private String legalDocumentId;

    @Field("download_history_list")
    private List<DowloadHistory> dowloadHistories;

    @Field("generated_legal_document")
    private GeneratedLegalDocument generatedLegalDocument;

    @Field("user_id")
    private String userId;

    @Field("order_identifier")
    private String orderIdentifier;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Order paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public Order totalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Float getPrice() {
        return price;
    }

    public Order price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getTax() {
        return tax;
    }

    public Order tax(Float tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Float tax) {
        this.tax = tax;
    }

    public String getInvoiceFilePath() {
        return invoiceFilePath;
    }

    public Order invoiceFilePath(String invoiceFilePath) {
        this.invoiceFilePath = invoiceFilePath;
        return this;
    }

    public void setInvoiceFilePath(String invoiceFilePath) {
        this.invoiceFilePath = invoiceFilePath;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Order status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getDestinationEmail() {
        return destinationEmail;
    }

    public Order destinationEmail(String destinationEmail) {
        this.destinationEmail = destinationEmail;
        return this;
    }

    public String getLegalDocumentId() {
        return legalDocumentId;
    }

    public void setLegalDocumentId(String legalDocumentId) {
        this.legalDocumentId = legalDocumentId;
    }

    public List<DowloadHistory> getDowloadHistories() {
        return dowloadHistories;
    }

    public void setDowloadHistories(List<DowloadHistory> dowloadHistories) {
        this.dowloadHistories = dowloadHistories;
    }

    public GeneratedLegalDocument getGeneratedLegalDocument() {
        return generatedLegalDocument;
    }

    public void setGeneratedLegalDocument(GeneratedLegalDocument generatedLegalDocument) {
        this.generatedLegalDocument = generatedLegalDocument;
    }

    public void setDestinationEmail(String destinationEmail) {
        this.destinationEmail = destinationEmail;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id='" + id + '\'' +
            ", paymentMethod=" + paymentMethod +
            ", totalPrice=" + totalPrice +
            ", price=" + price +
            ", tax=" + tax +
            ", invoiceFilePath='" + invoiceFilePath + '\'' +
            ", status=" + status +
            ", destinationEmail='" + destinationEmail + '\'' +
            ", legalDocumentId='" + legalDocumentId + '\'' +
            ", dowloadHistories=" + dowloadHistories +
            ", generatedLegalDocument=" + generatedLegalDocument +
            ", userId='" + userId + '\'' +
            ", OrderIdentifier=" + orderIdentifier +
            '}';
    }
}
