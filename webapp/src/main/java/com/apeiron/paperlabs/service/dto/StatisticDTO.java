package com.apeiron.paperlabs.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatisticDTO implements Serializable {

    private Map<String, Object> userStatistics = new HashMap<>();
    private Map<String, Object> orderStatistics = new HashMap<>();


    public StatisticDTO() {
    }

    public Map<String, Object> getUserStatistics() {
        return userStatistics;
    }

    public void setUserStatistics(Map<String, Object> userStatistics) {
        this.userStatistics = userStatistics;
    }


    public Map<String, Object> getOrderStatistics() {
        return orderStatistics;
    }

    public void setOrderStatistics(Map<String, Object> orderStatistics) {
        this.orderStatistics = orderStatistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticDTO that = (StatisticDTO) o;
        return Objects.equals(userStatistics, that.userStatistics) &&
            Objects.equals(orderStatistics, that.orderStatistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userStatistics, orderStatistics);
    }

    @Override
    public String toString() {
        return "StatisticDTO{" +
            "userStatistics=" + userStatistics +
            ", orderStatistics=" + orderStatistics +
            '}';
    }
}
