package com.airbyte.dorm.dto;

import java.io.Serializable;
import java.util.List;

public class PaymentHistoryQueryDTO implements Serializable {
    private List<String> paymentType;
    private String startDate;
    private String endDate;
    private Long count;

    public List<String> getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(List<String> paymentType) {
        this.paymentType = paymentType;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
