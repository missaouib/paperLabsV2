package com.apeiron.paperlabs.service.mapper;

import com.apeiron.paperlabs.domain.*;
import com.apeiron.paperlabs.service.dto.DescriptionSectionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DescriptionSection} and its DTO {@link DescriptionSectionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DescriptionSectionMapper extends EntityMapper<DescriptionSectionDTO, DescriptionSection> {



    default DescriptionSection fromId(String id) {
        if (id == null) {
            return null;
        }
        DescriptionSection descriptionSection = new DescriptionSection();
        descriptionSection.setId(id);
        return descriptionSection;
    }
}
