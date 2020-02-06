package com.apeiron.paperlabs.service.mapper;

import com.apeiron.paperlabs.domain.LegalDocument;
import com.apeiron.paperlabs.repository.LegalDocumentRepository;
import com.apeiron.paperlabs.service.CategoryService;
import com.apeiron.paperlabs.service.LawyerService;
import com.apeiron.paperlabs.service.dto.CategoryDTO;
import com.apeiron.paperlabs.service.dto.LawyerDTO;
import com.apeiron.paperlabs.service.dto.LegalDocumentDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Mapper for the entity {@link LegalDocument} and its DTO {@link LegalDocumentDTO}.
 */
@Service
public class LegalDocumentMapper {

    private final CategoryService categoryService;

    private final LegalDocumentRepository legalDocumentRepository;

    private final LawyerService lawyerService;

    private final DescriptionSectionMapper descriptionSectionMapper;

    public LegalDocumentMapper(
        CategoryService categoryService, LegalDocumentRepository legalDocumentRepository, LawyerService lawyerService,
        DescriptionSectionMapper descriptionSectionMapper
    ) {
        this.categoryService = categoryService;
        this.legalDocumentRepository = legalDocumentRepository;
        this.lawyerService = lawyerService;
        this.descriptionSectionMapper = descriptionSectionMapper;
    }

    public LegalDocument toEntity(LegalDocumentDTO dto) {
        if (dto == null) {
            return null;
        } else {
            LegalDocument legalDocument = new LegalDocument();
            legalDocument.setId(dto.getId());
            legalDocument.setShortName(dto.getShortName());
            legalDocument.setFullName(dto.getFullName());
            legalDocument.setKeywords(dto.getKeywords());
            legalDocument.setDescription(dto.getDescription());
            legalDocument.setTemplatePreviewPath(dto.getTemplatePreviewPath());
            legalDocument.setTemplateFilePath(dto.getTemplateFilePath());
            legalDocument.setStepperConfigFilePath(dto.getStepperConfigFilePath());
            legalDocument.setWorkflowConfigFilePath(dto.getWorkflowConfigFilePath());
            legalDocument.setPrice(dto.getPrice());
            legalDocument.setCreatedDate(dto.getCreatedDate());
            legalDocument.setAutoFillConcernedEntities(dto.getAutoFillConcernedEntities());
            legalDocument.setDescriptionSections(descriptionSectionMapper.toEntity(dto.getDescriptionSections()));
            legalDocument.setCompaniesAutoFillInputIdsByFieldName(dto.getCompaniesAutoFillInputIdsByFieldName());
            legalDocument.setEmployersAutoFillInputIdsByFieldName(dto.getEmployersAutoFillInputIdsByFieldName());

            List<String> documentsId = new ArrayList<>();

            if (dto.getDocumentsRecommendation()!=null) {
                for (LegalDocumentDTO legalDocumentDTO : dto.getDocumentsRecommendation()) {
                    documentsId.add(legalDocumentDTO.getId());
                }
                legalDocument.setDocumentsRecommendationId(documentsId);
            }



            if (dto.getCategoryDTO() != null) {
                legalDocument.setCategoryId(dto.getCategoryDTO().getId());
            }
            if (dto.getLawyerDTO() != null) {
                legalDocument.setLawyerId(dto.getLawyerDTO().getId());
            }

            return legalDocument;
        }
    }

    public LegalDocumentDTO toDto(LegalDocument entity) {
        if (entity == null) {
            return null;
        } else {
            LegalDocumentDTO legalDocumentDTO = new LegalDocumentDTO();
            legalDocumentDTO.setId(entity.getId());
            legalDocumentDTO.setShortName(entity.getShortName());
            legalDocumentDTO.setFullName(entity.getFullName());
            legalDocumentDTO.setKeywords(entity.getKeywords());
            legalDocumentDTO.setDescription(entity.getDescription());
            legalDocumentDTO.setTemplatePreviewPath(entity.getTemplatePreviewPath());
            legalDocumentDTO.setTemplateFilePath(entity.getTemplateFilePath());
            legalDocumentDTO.setStepperConfigFilePath(entity.getStepperConfigFilePath());
            legalDocumentDTO.setWorkflowConfigFilePath(entity.getWorkflowConfigFilePath());
            legalDocumentDTO.setPrice(entity.getPrice());
            legalDocumentDTO.setCreatedDate(entity.getCreatedDate());
            legalDocumentDTO.setAutoFillConcernedEntities(entity.getAutoFillConcernedEntities());
            legalDocumentDTO.setDescriptionSections(descriptionSectionMapper.toDto(entity.getDescriptionSections()));
            legalDocumentDTO.setCompaniesAutoFillInputIdsByFieldName(entity.getCompaniesAutoFillInputIdsByFieldName());
            legalDocumentDTO.setEmployersAutoFillInputIdsByFieldName(entity.getEmployersAutoFillInputIdsByFieldName());

            List<LegalDocumentDTO> documentDTOS = new ArrayList<>();

            for (String documentId : entity.getDocumentsRecommendationId()) {
                Optional<LegalDocument> optional =legalDocumentRepository.findById(documentId);

                if (optional.isPresent()) {
                    LegalDocumentDTO legalDoc = toDto(optional.get());
                    documentDTOS.add(legalDoc);

                }
            }
            legalDocumentDTO.setDocumentsRecommendation(documentDTOS);


            if (entity.getCategoryId() != null) {
                Optional<CategoryDTO> one = categoryService.findOne(entity.getCategoryId());

                if (one.isPresent()) {
                    legalDocumentDTO.setCategoryDTO(one.get());
                }

            }
            if (entity.getLawyerId() != null) {
                Optional<LawyerDTO> one = lawyerService.findOne(entity.getLawyerId());

                if (one.isPresent()) {
                    legalDocumentDTO.setLawyerDTO(one.get());
                }

            }


            return legalDocumentDTO;
        }
    }

    public List<LegalDocument> toEntity(List<LegalDocumentDTO> dtoList) {
        if (dtoList == null) {
            return null;
        } else {
            List<LegalDocument> list = new ArrayList(dtoList.size());
            Iterator var3 = dtoList.iterator();

            while (var3.hasNext()) {
                LegalDocumentDTO legalDocumentDTO = (LegalDocumentDTO) var3.next();
                list.add(this.toEntity(legalDocumentDTO));
            }

            return list;
        }
    }

    public List<LegalDocumentDTO> toDto(List<LegalDocument> entityList) {
        if (entityList == null) {
            return null;
        } else {
            List<LegalDocumentDTO> list = new ArrayList(entityList.size());
            Iterator var3 = entityList.iterator();

            while (var3.hasNext()) {
                LegalDocument legalDocument = (LegalDocument) var3.next();
                list.add(this.toDto(legalDocument));
            }

            return list;
        }
    }
}

