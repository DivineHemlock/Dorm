package com.airbyte.dorm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "insurance")
public class Insurance implements Serializable {

    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(255)") String insurerName;
    private @Column(columnDefinition = "VARCHAR(255)") String insuranceCode;
    private @Column(columnDefinition = "VARCHAR(255)") String insurerOrganization;
    private @Column(columnDefinition = "VARCHAR(255)") String employer;
    //private @JsonIgnore
    //@OneToMany(mappedBy ="insurance", fetch = FetchType.LAZY) List<PaymentHistory> paymentHistoryList = new ArrayList<>();

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

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

    //public List<PaymentHistory> getPaymentHistoryList() {
    //    return paymentHistoryList;
    //}
//
    //public void setPaymentHistoryList(List<PaymentHistory> paymentHistoryList) {
    //    this.paymentHistoryList = paymentHistoryList;
    //}
}
