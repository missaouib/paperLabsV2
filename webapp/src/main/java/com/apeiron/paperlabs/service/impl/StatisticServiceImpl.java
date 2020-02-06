package com.apeiron.paperlabs.service.impl;

import com.apeiron.paperlabs.domain.Order;
import com.apeiron.paperlabs.domain.enumeration.OrderStatus;
import com.apeiron.paperlabs.repository.OrderRepository;
import com.apeiron.paperlabs.repository.UserRepository;
import com.apeiron.paperlabs.service.StatisticService;
import com.apeiron.paperlabs.service.dto.DocumentInfoStatisticDTO;
import com.apeiron.paperlabs.service.dto.StatisticDTO;
import com.apeiron.paperlabs.service.dto.UserInfoStatisticDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class StatisticServiceImpl implements StatisticService {

    private final Logger log = LoggerFactory.getLogger(StatisticServiceImpl.class);

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    @Autowired
    MongoTemplate mongoTemplate;


    public StatisticServiceImpl(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Optional<StatisticDTO> getStatisticsOrder() {
        StatisticDTO statisticDTO = new StatisticDTO();
        log.debug("REST request to get statistics for Order");
        HashMap<String, Object> orderStatistic = new HashMap<String, Object>();
        orderStatistic.put("WaitingOrderToday", orderRepository.findAllByStatusEqualsAndGeneratedLegalDocument_LastModifiedDateAfter(OrderStatus.WAITING, Instant.now().truncatedTo(ChronoUnit.DAYS)).size());
        orderStatistic.put("PaidOrderToday", orderRepository.findAllByStatusEqualsAndGeneratedLegalDocument_LastModifiedDateAfter(OrderStatus.PAID, Instant.now().truncatedTo(ChronoUnit.DAYS)).size());
        orderStatistic.put("AbandonedOrderToday", orderRepository.findAllByStatusEqualsAndGeneratedLegalDocument_LastModifiedDateAfter(OrderStatus.ABANDONED, Instant.now().truncatedTo(ChronoUnit.DAYS)).size());
        statisticDTO.setOrderStatistics(orderStatistic);

        return Optional.of(statisticDTO);
    }

    @Override
    public Optional<StatisticDTO> getStatisticsUser() {

        StatisticDTO statisticDTO = new StatisticDTO();
        log.debug("REST request to get statistics for User");
        HashMap<String, Object> userStatistic = new HashMap<String, Object>();
        userStatistic.put("NumberOfUsers", userRepository.count());
        userStatistic.put("UsersNotActivated", userRepository.findAllByActivatedIsFalse().size());
        userStatistic.put("UsersAddedToday", userRepository.findAllByCreatedDateAfter(Instant.now().truncatedTo(ChronoUnit.DAYS)).size());
        statisticDTO.setUserStatistics(userStatistic);
        return Optional.of(statisticDTO);
    }

    @Override
    public UserInfoStatisticDTO getStatisticsUserDetails(String userId) {

        UserInfoStatisticDTO userInfoStatisticDTO = new UserInfoStatisticDTO();


        Aggregation paidOrderAggregation = newAggregation(
            match(Criteria.where("userId").is(userId).and("status").is(OrderStatus.PAID)),
            count().as("numberPaidOrder"),
            sort(Sort.Direction.ASC, "numberPaidOrder"));

        AggregationResults<UserInfoStatisticDTO> groupPaidResults = mongoTemplate.aggregate(
            paidOrderAggregation, Order.class, UserInfoStatisticDTO.class);

        if (groupPaidResults.getUniqueMappedResult() != null) {
            userInfoStatisticDTO.setNumberPaidOrder(groupPaidResults.getUniqueMappedResult().getNumberPaidOrder());
        } else {
            userInfoStatisticDTO.setNumberPaidOrder(0);
        }

        Aggregation abandonedOrderAggregation = newAggregation(
            match(Criteria.where("userId").is(userId).and("status").is(OrderStatus.ABANDONED)),
            count().as("numberAbandonedOrder"),
            sort(Sort.Direction.ASC, "numberAbandonedOrder"));

        AggregationResults<UserInfoStatisticDTO> groupAbandonedResults = mongoTemplate.aggregate(
            abandonedOrderAggregation, Order.class, UserInfoStatisticDTO.class);

        if (groupAbandonedResults.getUniqueMappedResult() != null) {
            userInfoStatisticDTO.setNumberAbandonedOrder(groupAbandonedResults.getUniqueMappedResult().getNumberAbandonedOrder());
        } else {
            userInfoStatisticDTO.setNumberAbandonedOrder(0);
        }

        Aggregation sumPaidOrderAggregation = newAggregation(
            match(Criteria.where("userId").is(userId).and("status").is(OrderStatus.PAID)),
            group("totalPrice").sum("totalPrice").as("sumAllPaidOrder"),
            sort(Sort.Direction.ASC, "totalPrice"));

        AggregationResults<UserInfoStatisticDTO> groupSumPaidResults = mongoTemplate.aggregate(
            sumPaidOrderAggregation, Order.class, UserInfoStatisticDTO.class);

        if (groupSumPaidResults.getUniqueMappedResult() != null) {

            userInfoStatisticDTO.setSumAllPaidOrder(groupSumPaidResults.getUniqueMappedResult().getSumAllPaidOrder());
        } else {
            userInfoStatisticDTO.setSumAllPaidOrder(0.0);
        }


        return userInfoStatisticDTO;
    }

    @Override
    public DocumentInfoStatisticDTO getStatisticsDocumentDetails(String documentId) {

        DocumentInfoStatisticDTO documentInfoStatisticDTO = new DocumentInfoStatisticDTO();

        List<Order> paidOrderList = orderRepository.findAllByLegalDocumentIdEqualsAndStatusEquals(documentId, OrderStatus.PAID);

        Aggregation canceledDownloadAggregation = newAggregation(
            match(Criteria.where("legalDocumentId").is(documentId).and("status").is(OrderStatus.ABANDONED)),
            count().as("numberOfCanceledDownload"),
            sort(Sort.Direction.ASC, "numberOfCanceledDownload"));

        AggregationResults<DocumentInfoStatisticDTO> groupCancelResults = mongoTemplate.aggregate(
            canceledDownloadAggregation, Order.class, DocumentInfoStatisticDTO.class);

        if (groupCancelResults.getUniqueMappedResult() != null) {
            documentInfoStatisticDTO.setNumberOfCanceledDownload(groupCancelResults.getUniqueMappedResult().getNumberOfCanceledDownload());
        } else {
            documentInfoStatisticDTO.setNumberOfCanceledDownload(0);
        }
        double sumPaidOrder = 0.0;
        for (Order order : paidOrderList) {
            sumPaidOrder += order.getTotalPrice();
        }
        documentInfoStatisticDTO.setSumDocumentRevenue(sumPaidOrder);

        int numberOfDownload = 0;
        for (Order order : paidOrderList) {
            numberOfDownload +=  order.getDowloadHistories().size();

        }
        documentInfoStatisticDTO.setNumberOfDownload(numberOfDownload);

        return documentInfoStatisticDTO;
    }


}
