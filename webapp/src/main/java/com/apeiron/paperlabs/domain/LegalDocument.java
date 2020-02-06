package com.apeiron.paperlabs.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * A LegalDocument.
 */
@Document(collection = "legal_document")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "legaldocument")
public class LegalDocument extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Field("short_name")
    private String shortName;

    @NotNull
    @Field("full_name")
    private String fullName;

    @Field("keywords")
    private String keywords;

    @NotNull
    @Field("description")
    private String description;

    @NotNull
    @Field("template_preview_path")
    private String templatePreviewPath;

    @NotNull
    @Field("template_file_path")
    private String templateFilePath;

    @NotNull
    @Field("stepper_config_file_path")
    private String stepperConfigFilePath;

    @NotNull
    @Field("workflow_config_file_path")
    private String workflowConfigFilePath;

    @Field("category_id")
    private String categoryId;

    @Field("lawyer_id")
    private String lawyerId;

    private List<DescriptionSection> descriptionSections;

    @Field("auto_fill_concerned_entities")
    private List<String> autoFillConcernedEntities;

    @Field("price")
    private Float price;

    @Field("company_input_ids_by_field_name")
    private List<Map<String, String>> companiesAutoFillInputIdsByFieldName;

    @Field("employer_input_ids_by_field_name")
    private List<Map<String, String>> employersAutoFillInputIdsByFieldName;


    @Field("documents_recommendation")
    private List<String> documentsRecommendationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public LegalDocument shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public LegalDocument fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getKeywords() {
        return keywords;
    }

    public LegalDocument keywords(String keywords) {
        this.keywords = keywords;
        return this;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public LegalDocument description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplatePreviewPath() {
        return templatePreviewPath;
    }

    public LegalDocument templatePreviewPath(String templatePreviewPath) {
        this.templatePreviewPath = templatePreviewPath;
        return this;
    }

    public void setTemplatePreviewPath(String templatePreviewPath) {
        this.templatePreviewPath = templatePreviewPath;
    }

    public String getTemplateFilePath() {
        return templateFilePath;
    }

    public LegalDocument templateFilePath(String templateFilePath) {
        this.templateFilePath = templateFilePath;
        return this;
    }

    public void setTemplateFilePath(String templateFilePath) {
        this.templateFilePath = templateFilePath;
    }

    public String getStepperConfigFilePath() {
        return stepperConfigFilePath;
    }

    public LegalDocument stepperConfigFilePath(String stepperConfigFilePath) {
        this.stepperConfigFilePath = stepperConfigFilePath;
        return this;
    }

    public void setStepperConfigFilePath(String stepperConfigFilePath) {
        this.stepperConfigFilePath = stepperConfigFilePath;
    }

    public String getWorkflowConfigFilePath() {
        return workflowConfigFilePath;
    }

    public LegalDocument workflowConfigFilePath(String workflowConfigFilePath) {
        this.workflowConfigFilePath = workflowConfigFilePath;
        return this;
    }

    public void setWorkflowConfigFilePath(String workflowConfigFilePath) {
        this.workflowConfigFilePath = workflowConfigFilePath;
    }

    public Float getPrice() {
        return price;
    }

    public LegalDocument price(Float price) {
        this.price = price;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public String getLawyerId() {
        return lawyerId;
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerId = lawyerId;
    }

    public List<DescriptionSection> getDescriptionSections() {
        return descriptionSections;
    }

    public void setDescriptionSections(List<DescriptionSection> descriptionSections) {
        this.descriptionSections = descriptionSections;
    }

    public List<String> getAutoFillConcernedEntities() {
        return autoFillConcernedEntities;
    }

    public void setAutoFillConcernedEntities(List<String> autoFillConcernedEntities) {
        this.autoFillConcernedEntities = autoFillConcernedEntities;
    }

    public List<String> getDocumentsRecommendationId() {
        return documentsRecommendationId;
    }

    public void setDocumentsRecommendationId(List<String> documentsRecommendationId) {
        this.documentsRecommendationId = documentsRecommendationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LegalDocument)) {
            return false;
        }
        return id != null && id.equals(((LegalDocument) o).id);
    }

    public List<Map<String, String>> getCompaniesAutoFillInputIdsByFieldName() {
        return companiesAutoFillInputIdsByFieldName;
    }

    public void setCompaniesAutoFillInputIdsByFieldName(List<Map<String, String>> companiesAutoFillInputIdsByFieldName) {
        this.companiesAutoFillInputIdsByFieldName = companiesAutoFillInputIdsByFieldName;
    }

    public List<Map<String, String>> getEmployersAutoFillInputIdsByFieldName() {
        return employersAutoFillInputIdsByFieldName;
    }

    public void setEmployersAutoFillInputIdsByFieldName(List<Map<String, String>> employersAutoFillInputIdsByFieldName) {
        this.employersAutoFillInputIdsByFieldName = employersAutoFillInputIdsByFieldName;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LegalDocument{" +
            "id='" + id + '\'' +
            ", shortName='" + shortName + '\'' +
            ", fullName='" + fullName + '\'' +
            ", keywords='" + keywords + '\'' +
            ", description='" + description + '\'' +
            ", templatePreviewPath='" + templatePreviewPath + '\'' +
            ", templateFilePath='" + templateFilePath + '\'' +
            ", stepperConfigFilePath='" + stepperConfigFilePath + '\'' +
            ", workflowConfigFilePath='" + workflowConfigFilePath + '\'' +
            ", categoryId='" + categoryId + '\'' +
            ", lawyerId='" + lawyerId + '\'' +
            ", descriptionSections=" + descriptionSections +
            ", autoFillConcernedEntities=" + autoFillConcernedEntities +
            ", price=" + price +
            ", companiesAutoFillInputIdsByFieldName=" + companiesAutoFillInputIdsByFieldName +
            ", employersAutoFillInputIdsByFieldName=" + employersAutoFillInputIdsByFieldName +
            ", documentsRecommendationId=" + documentsRecommendationId +
            '}';
    }
}
