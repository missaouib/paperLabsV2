package com.apeiron.paperlabs.service.mapper;

import com.apeiron.paperlabs.domain.*;
import com.apeiron.paperlabs.service.dto.LawyerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lawyer} and its DTO {@link LawyerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LawyerMapper extends EntityMapper<LawyerDTO, Lawyer> {



    default Lawyer fromId(String id) {
        if (id == null) {
            return null;
        }
        Lawyer lawyer = new Lawyer();
        lawyer.setId(id);
        return lawyer;
    }
}
