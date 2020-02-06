package com.apeiron.paperlabs.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Employer.
 */
@Document(collection = "employer")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "employer")
public class Employer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("employer_cin_number")
    private Long employerCinNumber;

    @Field("employer_full_name")
    private String employerFullName;

    @Field("employer_full_address")
    private String employerFullAddress;

    @Field("user_id")
    private String userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEmployerCinNumber() {
        return employerCinNumber;
    }

    public Employer employerCinNumber(Long employerCinNumber) {
        this.employerCinNumber = employerCinNumber;
        return this;
    }

    public void setEmployerCinNumber(Long employerCinNumber) {
        this.employerCinNumber = employerCinNumber;
    }

    public String getEmployerFullName() {
        return employerFullName;
    }

    public Employer employerFullName(String employerFullName) {
        this.employerFullName = employerFullName;
        return this;
    }

    public void setEmployerFullName(String employerFullName) {
        this.employerFullName = employerFullName;
    }

    public String getEmployerFullAddress() {
        return employerFullAddress;
    }

    public Employer employerFullAddress(String employerFullAddress) {
        this.employerFullAddress = employerFullAddress;
        return this;
    }

    public void setEmployerFullAddress(String employerFullAddress) {
        this.employerFullAddress = employerFullAddress;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employer)) {
            return false;
        }
        return id != null && id.equals(((Employer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employer{" +
            "id='" + id + '\'' +
            ", employerCinNumber=" + employerCinNumber +
            ", employerFullName='" + employerFullName + '\'' +
            ", employerFullAddress='" + employerFullAddress + '\'' +
            ", userId='" + userId + '\'' +
            '}';
    }
}
