package com.apeiron.paperlabs.service.mapper;

import com.apeiron.paperlabs.domain.*;
import com.apeiron.paperlabs.service.dto.GeneratedLegalDocumentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link GeneratedLegalDocument} and its DTO {@link GeneratedLegalDocumentDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GeneratedLegalDocumentMapper extends EntityMapper<GeneratedLegalDocumentDTO, GeneratedLegalDocument> {



    default GeneratedLegalDocument fromId(String id) {
        if (id == null) {
            return null;
        }
        GeneratedLegalDocument generatedLegalDocument = new GeneratedLegalDocument();
        generatedLegalDocument.setId(id);
        return generatedLegalDocument;
    }
}
