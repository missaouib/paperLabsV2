package com.apeiron.paperlabs.service.mapper;

import com.apeiron.paperlabs.domain.*;
import com.apeiron.paperlabs.service.dto.DowloadHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DowloadHistory} and its DTO {@link DowloadHistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DowloadHistoryMapper extends EntityMapper<DowloadHistoryDTO, DowloadHistory> {



    default DowloadHistory fromId(String id) {
        if (id == null) {
            return null;
        }
        DowloadHistory dowloadHistory = new DowloadHistory();
        dowloadHistory.setId(id);
        return dowloadHistory;
    }
}
