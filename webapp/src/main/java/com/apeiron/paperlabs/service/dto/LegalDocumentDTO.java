package com.apeiron.paperlabs.service.dto;

import com.apeiron.paperlabs.domain.LegalDocument;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the {@link com.apeiron.paperlabs.domain.LegalDocument} entity.
 */
public class LegalDocumentDTO implements Serializable {

    private String id;

    @NotNull
    private String shortName;

    @NotNull
    private String fullName;

    private String keywords;

    @NotNull
    private String description;

    @NotNull
    private String templatePreviewPath;

    @NotNull
    private String templateFilePath;

    @NotNull
    private String stepperConfigFilePath;

    @NotNull
    private String workflowConfigFilePath;

    private CategoryDTO categoryDTO;

    private LawyerDTO lawyerDTO;

    private Float price;

    private List<String> autoFillConcernedEntities;

    private List<DescriptionSectionDTO> descriptionSections;

    private List<Map<String, String>> companiesAutoFillInputIdsByFieldName;

    private List<Map<String, String>> employersAutoFillInputIdsByFieldName;

    private List<LegalDocumentDTO> documentsRecommendation;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplatePreviewPath() {
        return templatePreviewPath;
    }

    public void setTemplatePreviewPath(String templatePreviewPath) {
        this.templatePreviewPath = templatePreviewPath;
    }

    public String getTemplateFilePath() {
        return templateFilePath;
    }

    public void setTemplateFilePath(String templateFilePath) {
        this.templateFilePath = templateFilePath;
    }

    public String getStepperConfigFilePath() {
        return stepperConfigFilePath;
    }

    public void setStepperConfigFilePath(String stepperConfigFilePath) {
        this.stepperConfigFilePath = stepperConfigFilePath;
    }

    public String getWorkflowConfigFilePath() {
        return workflowConfigFilePath;
    }

    public void setWorkflowConfigFilePath(String workflowConfigFilePath) {
        this.workflowConfigFilePath = workflowConfigFilePath;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public LawyerDTO getLawyerDTO() {
        return lawyerDTO;
    }

    public void setLawyerDTO(LawyerDTO lawyerDTO) {
        this.lawyerDTO = lawyerDTO;
    }

    public List<DescriptionSectionDTO> getDescriptionSections() {
        return descriptionSections;
    }

    public void setDescriptionSections(List<DescriptionSectionDTO> descriptionSections) {
        this.descriptionSections = descriptionSections;
    }

    public List<LegalDocumentDTO> getDocumentsRecommendation() {
        return documentsRecommendation;
    }

    public void setDocumentsRecommendation(List<LegalDocumentDTO> documentsRecommendation) {
        this.documentsRecommendation = documentsRecommendation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LegalDocumentDTO legalDocumentDTO = (LegalDocumentDTO) o;
        if (legalDocumentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), legalDocumentDTO.getId());
    }

    public List<String> getAutoFillConcernedEntities() {
        return autoFillConcernedEntities;
    }

    public void setAutoFillConcernedEntities(List<String> autoFillConcernedEntities) {
        this.autoFillConcernedEntities = autoFillConcernedEntities;
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

    public void setEmployersAutoFillInputIdsByFieldName(List<Map<String, String>> employeesAutoFillInputIdsByFieldName) {
        this.employersAutoFillInputIdsByFieldName = employeesAutoFillInputIdsByFieldName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LegalDocumentDTO{" +
            "id='" + id + '\'' +
            ", shortName='" + shortName + '\'' +
            ", fullName='" + fullName + '\'' +
            ", keywords='" + keywords + '\'' +
            ", description='" + description + '\'' +
            ", templatePreviewPath='" + templatePreviewPath + '\'' +
            ", templateFilePath='" + templateFilePath + '\'' +
            ", stepperConfigFilePath='" + stepperConfigFilePath + '\'' +
            ", workflowConfigFilePath='" + workflowConfigFilePath + '\'' +
            ", categoryDTO=" + categoryDTO +
            ", lawyerDTO=" + lawyerDTO +
            ", price=" + price +
            ", autoFillConcernedEntities=" + autoFillConcernedEntities +
            ", descriptionSections=" + descriptionSections +
            ", companiesAutoFillInputIdsByFieldName=" + companiesAutoFillInputIdsByFieldName +
            ", employersAutoFillInputIdsByFieldName=" + employersAutoFillInputIdsByFieldName +
            ", documentsRecommendation=" + documentsRecommendation +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            '}';
    }
}
