package com.apeiron.paperlabs.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.apeiron.paperlabs.domain.GeneratedLegalDocument} entity.
 */
public class GeneratedLegalDocumentDTO implements Serializable {

    private String id;

    @NotNull
    private String generatedWordFilePath;

    private String genratedPDFFilePath;

    private Instant date;

    private Instant paymentDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeneratedWordFilePath() {
        return generatedWordFilePath;
    }

    public void setGeneratedWordFilePath(String generatedWordFilePath) {
        this.generatedWordFilePath = generatedWordFilePath;
    }

    public String getGenratedPDFFilePath() {
        return genratedPDFFilePath;
    }

    public void setGenratedPDFFilePath(String genratedPDFFilePath) {
        this.genratedPDFFilePath = genratedPDFFilePath;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GeneratedLegalDocumentDTO generatedLegalDocumentDTO = (GeneratedLegalDocumentDTO) o;
        if (generatedLegalDocumentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), generatedLegalDocumentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    @Override
    public String toString() {
        return "GeneratedLegalDocumentDTO{" +
            "id='" + id + '\'' +
            ", generatedWordFilePath='" + generatedWordFilePath + '\'' +
            ", genratedPDFFilePath='" + genratedPDFFilePath + '\'' +
            ", date=" + date +
            ", paymentDate=" + paymentDate +
            '}';
    }
}
