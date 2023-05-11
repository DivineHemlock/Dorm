package com.airbyte.dorm.dto;

import java.util.List;

public class InsuranceDTO extends ParentDTO{

    private String insurerName;
    private String insuranceCode;
    private String insurerOrganization;
    private String employer;
    private String humanId;
    private List<String> paymentHistoryList;

    public String getInsurerName() {
        return insurerName;
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public String getInsuranceCode() {
        return insuranceCode;
    }

    public void setInsuranceCode(String insuranceCode) {
        this.insuranceCode = insuranceCode;
    }

    public String getInsurerOrganization() {
        return insurerOrganization;
    }

    public void setInsurerOrganization(String insurerOrganization) {
        this.insurerOrganization = insurerOrganization;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getHumanId() {
        return humanId;
    }

    public void setHumanId(String humanId) {
        this.humanId = humanId;
    }

    public List<String> getPaymentHistoryList() {
        return paymentHistoryList;
    }

    public void setPaymentHistoryList(List<String> paymentHistoryList) {
        this.paymentHistoryList = paymentHistoryList;
    }
}
