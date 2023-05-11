package com.airbyte.dorm.dto;

import javax.validation.constraints.NotBlank;

public class RecordDTO extends ParentDTO {
    private String date;
    private String hour;
    private String startDate;
    private String endDate;
    private String title;
    private String destinationAddress;
    private String destinationPhoneNumber;
    private String description;
    private String penaltyAmount;
    private String penaltyType;
    private String returnedAmount;
    private @NotBlank String personId;
    private Boolean checkCleaning;
    private String relation;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationPhoneNumber() {
        return destinationPhoneNumber;
    }

    public void setDestinationPhoneNumber(String destinationPhoneNumber) {
        this.destinationPhoneNumber = destinationPhoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(String penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Boolean getCheckCleaning() {
        return checkCleaning;
    }

    public void setCheckCleaning(Boolean checkCleaning) {
        this.checkCleaning = checkCleaning;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(String penaltyType) {
        this.penaltyType = penaltyType;
    }

    public String getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(String returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
