package com.apeiron.paperlabs.domain;
import com.apeiron.paperlabs.domain.enumeration.CompanyExportability;
import com.apeiron.paperlabs.domain.enumeration.CompanyJurisdiction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.List;

/**
 * A Company.
 */
@Document(collection = "company")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("company_name")
    private String companyName;

    @Field("company_capital")
    private Long companyCapital;

    @Field("company_unique_identification")
    private String companyUniqueIdentification;

    @Field("company_representative_full_name")
    private String companyRepresentativeFullName;

    @Field("company_full_address")
    private String companyFullAddress;

    @Field("company_type")
    private String companyType;

    @Field("user_id")
    private String userId;

    @Field("company_activity_area")
    private String companyActivityArea;

    @Field("company_founding_partners")
    private List<String> companyFoundingPartners;

    @Field("company_exportability")
    private String companyExportability;

    @Field("company_jurisdiction")
    private String companyJurisdiction;

    @Field("company_manager_full_name")
    private String companyManagerFullName;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Company companyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCompanyCapital() {
        return companyCapital;
    }

    public Company companyCapital(Long companyCapital) {
        this.companyCapital = companyCapital;
        return this;
    }

    public void setCompanyCapital(Long companyCapital) {
        this.companyCapital = companyCapital;
    }

    public String getCompanyUniqueIdentification() {
        return companyUniqueIdentification;
    }

    public Company companyUniqueIdentification(String companyUniqueIdentification) {
        this.companyUniqueIdentification = companyUniqueIdentification;
        return this;
    }

    public void setCompanyUniqueIdentification(String companyUniqueIdentification) {
        this.companyUniqueIdentification = companyUniqueIdentification;
    }

    public String getCompanyRepresentativeFullName() {
        return companyRepresentativeFullName;
    }

    public Company companyRepresentativeFullName(String companyRepresentativeFullName) {
        this.companyRepresentativeFullName = companyRepresentativeFullName;
        return this;
    }

    public void setCompanyRepresentativeFullName(String companyRepresentativeFullName) {
        this.companyRepresentativeFullName = companyRepresentativeFullName;
    }

    public String getCompanyFullAddress() {
        return companyFullAddress;
    }

    public Company companyFullAddress(String companyFullAddress) {
        this.companyFullAddress = companyFullAddress;
        return this;
    }

    public void setCompanyFullAddress(String companyFullAddress) {
        this.companyFullAddress = companyFullAddress;
    }

    public String getCompanyType() {
        return companyType;
    }

    public Company companyType(String companyType) {
        this.companyType = companyType;
        return this;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public String getUserId() {
        return userId;
    }
    public Company companyUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyActivityArea() {
        return companyActivityArea;
    }

    public void setCompanyActivityArea(String companyActivityArea) {
        this.companyActivityArea = companyActivityArea;
    }

    public List<String> getCompanyFoundingPartners() {
        return companyFoundingPartners;
    }

    public void setCompanyFoundingPartners(List<String> companyFoundingPartners) {
        this.companyFoundingPartners = companyFoundingPartners;
    }

    public String getCompanyExportability() {
        return companyExportability;
    }

    public void setCompanyExportability(String companyExportability) {
        this.companyExportability = companyExportability;
    }

    public String getCompanyJurisdiction() {
        return companyJurisdiction;
    }

    public void setCompanyJurisdiction(String companyJurisdiction) {
        this.companyJurisdiction = companyJurisdiction;
    }

    public String getCompanyManagerFullName() {
        return companyManagerFullName;
    }

    public void setCompanyManagerFullName(String companyManagerFullName) {
        this.companyManagerFullName = companyManagerFullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Company{" +
            "id='" + id + '\'' +
            ", companyName='" + companyName + '\'' +
            ", companyCapital=" + companyCapital +
            ", companyUniqueIdentification='" + companyUniqueIdentification + '\'' +
            ", companyRepresentativeFullName='" + companyRepresentativeFullName + '\'' +
            ", companyFullAddress='" + companyFullAddress + '\'' +
            ", companyType='" + companyType + '\'' +
            ", userId='" + userId + '\'' +
            ", companyActivityArea='" + companyActivityArea + '\'' +
            ", companyFoundingPartners=" + companyFoundingPartners +
            ", companyExportability=" + companyExportability +
            ", companyJurisdiction=" + companyJurisdiction +
            ", companyManagerFullName='" + companyManagerFullName + '\'' +
            '}';
    }
}
