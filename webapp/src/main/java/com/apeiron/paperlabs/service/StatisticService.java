package com.apeiron.paperlabs.service;



import com.apeiron.paperlabs.service.dto.DocumentInfoStatisticDTO;
import com.apeiron.paperlabs.service.dto.StatisticDTO;
import com.apeiron.paperlabs.service.dto.UserInfoStatisticDTO;

import java.util.Optional;


/**
 * Interface for the Statistic Service
 */
public interface StatisticService {

    Optional<StatisticDTO> getStatisticsOrder();

    Optional<StatisticDTO> getStatisticsUser();

    UserInfoStatisticDTO getStatisticsUserDetails(String userId);

    DocumentInfoStatisticDTO getStatisticsDocumentDetails(String documentId);

}
