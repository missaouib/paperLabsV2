package com.apeiron.paperlabs.web.rest;

import com.apeiron.paperlabs.service.StatisticService;
import com.apeiron.paperlabs.service.dto.DocumentInfoStatisticDTO;
import com.apeiron.paperlabs.service.dto.StatisticDTO;
import com.apeiron.paperlabs.service.dto.UserInfoStatisticDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Resource Class for The Staistic Service
 *
 * @author Ghandour Abdelkrim
 * @version %I%, %G%
 * @since 0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api")
public class StatisticResource {

    private final Logger log = LoggerFactory.getLogger(StatisticResource.class);

    private final StatisticService statisticService;

    public StatisticResource(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/statistic/user")
    public ResponseEntity<StatisticDTO> getUsersStatistics() {
        log.debug("REST request to get user statistics");
        Optional<StatisticDTO> statisticDTO = statisticService.getStatisticsUser();
        return ResponseUtil.wrapOrNotFound(statisticDTO);
    }

    @GetMapping("/statistic/order")
    public ResponseEntity<StatisticDTO> getOrderStatistics() {
        log.debug("REST request to get order statistics");
        Optional<StatisticDTO> statisticDTO = statisticService.getStatisticsOrder();
        return ResponseUtil.wrapOrNotFound(statisticDTO);
    }

    @GetMapping("/statistic/user/{userId}")
    public UserInfoStatisticDTO getUserStatisticsInfo(@PathVariable String userId) {
        log.debug("REST request to get user order info : {}", userId);
        return statisticService.getStatisticsUserDetails(userId);
    }

    @GetMapping("/statistic/document/{documentId}")
    public DocumentInfoStatisticDTO getDocumentStatisticsInfo(@PathVariable String documentId) {
        log.debug("REST request to get user order info : {}", documentId);
        return statisticService.getStatisticsDocumentDetails(documentId);
    }

}
