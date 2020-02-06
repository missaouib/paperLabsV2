package com.apeiron.paperlabs.service.dto;

import java.util.Objects;

public class UserInfoStatisticDTO {

    private Integer numberPaidOrder;
    private Double sumAllPaidOrder;
    private Integer numberAbandonedOrder;


    public Integer getNumberPaidOrder() {
        return numberPaidOrder;
    }

    public void setNumberPaidOrder(Integer numberPaidOrder) {
        this.numberPaidOrder = numberPaidOrder;
    }

    public Double getSumAllPaidOrder() {
        return sumAllPaidOrder;
    }

    public void setSumAllPaidOrder(Double sumAllPaidOrder) {
        this.sumAllPaidOrder = sumAllPaidOrder;
    }

    public Integer getNumberAbandonedOrder() {
        return numberAbandonedOrder;
    }

    public void setNumberAbandonedOrder(Integer numberAbandonedOrder) {
        this.numberAbandonedOrder = numberAbandonedOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoStatisticDTO that = (UserInfoStatisticDTO) o;
        return numberPaidOrder == that.numberPaidOrder &&
            numberAbandonedOrder == that.numberAbandonedOrder &&
            Objects.equals(sumAllPaidOrder, that.sumAllPaidOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberPaidOrder, sumAllPaidOrder, numberAbandonedOrder);
    }

    @Override
    public String toString() {
        return "UserInfoStatisticDTO{" +
            "numberPaidOrder=" + numberPaidOrder +
            ", sumAllPaidOrder=" + sumAllPaidOrder +
            ", numberAbandonedOrder=" + numberAbandonedOrder +
            '}';
    }
}
