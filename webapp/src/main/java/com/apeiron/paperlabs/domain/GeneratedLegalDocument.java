package com.apeiron.paperlabs.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A GeneratedLegalDocument.
 */
@org.springframework.data.elasticsearch.annotations.Document(indexName = "generatedlegaldocument")
public class GeneratedLegalDocument extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Field("generated_word_file_path")
    private String generatedWordFilePath;

    @Field("genrated_pdf_file_path")
    private String genratedPDFFilePath;

    @Field("date")
    private Instant date;

    @Field("payment_date")
    private Instant paymentDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeneratedWordFilePath() {
        return generatedWordFilePath;
    }

    public GeneratedLegalDocument generatedWordFilePath(String generatedWordFilePath) {
        this.generatedWordFilePath = generatedWordFilePath;
        return this;
    }

    public void setGeneratedWordFilePath(String generatedWordFilePath) {
        this.generatedWordFilePath = generatedWordFilePath;
    }

    public String getGenratedPDFFilePath() {
        return genratedPDFFilePath;
    }

    public GeneratedLegalDocument genratedPDFFilePath(String genratedPDFFilePath) {
        this.genratedPDFFilePath = genratedPDFFilePath;
        return this;
    }

    public void setGenratedPDFFilePath(String genratedPDFFilePath) {
        this.genratedPDFFilePath = genratedPDFFilePath;
    }

    public Instant getDate() {
        return date;
    }

    public GeneratedLegalDocument date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeneratedLegalDocument)) {
            return false;
        }
        return id != null && id.equals(((GeneratedLegalDocument) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "GeneratedLegalDocument{" +
            "id='" + id + '\'' +
            ", generatedWordFilePath='" + generatedWordFilePath + '\'' +
            ", genratedPDFFilePath='" + genratedPDFFilePath + '\'' +
            ", date=" + date +
            ", paymentDate=" + paymentDate +
            '}';
    }
}
