package com.airbyte.dorm.model;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.model.enums.PenaltyType;
import com.airbyte.dorm.model.people.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table
public class Record implements Serializable {
    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column String date;
    private @Column String hour;
    private @Column Instant startDate;
    private @Column Instant endDate;
    private @Column(columnDefinition = "VARCHAR(100)") String title;
    private @Column(columnDefinition = "VARCHAR(1000)") String destinationAddress;
    private @Column(columnDefinition = "VARCHAR(50)") String destinationPhoneNumber;
    private @Column(columnDefinition = "VARCHAR(1000)") String description;
    private @Column(columnDefinition = "VARCHAR(1000)") String penaltyAmount;
    private @Column(columnDefinition = "VARCHAR(255)") String returnedAmount;
    private @Enumerated (EnumType.STRING) @Column (columnDefinition = "VARCHAR(20)") PenaltyType penaltyType;
    private @JsonIgnore
    @JoinColumn(name = "person")
    @ManyToOne() Person person;
    private @Column Boolean checkCleaning;
    private @Column(columnDefinition = "VARCHAR(255)") String relation;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public void setPenaltyAmount(String penalty) {
        this.penaltyAmount = penalty;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PenaltyType getPenaltyType() {
        return penaltyType;
    }

    public void setPenaltyType(PenaltyType penaltyType) {
        this.penaltyType = penaltyType;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(String returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    @JsonIgnore
    public Instant startDate() {
        return startDate;
    }

    public String getStartDate() {
        if (this.startDate != null) {
            Date start = Date.from(this.startDate);
            SimpleDateFormat format = new SimpleDateFormat(TimeConverter.UPDATED_PATTERN_FORMAT);
            return format.format(start);
        }
        return null;
    }


    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    @JsonIgnore
    public Instant endDate() {
        return endDate;
    }

    public String getEndDate() {
        if (this.endDate != null) {
            Date end = Date.from(this.endDate);
            SimpleDateFormat format = new SimpleDateFormat(TimeConverter.UPDATED_PATTERN_FORMAT);
            return format.format(end);
        }
        return null;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    @JsonProperty("personId")
    public String getPersonId() {
        if (this.person != null) {
            return this.person.getId();
        }
        return null;
    }

    public Boolean getCheckCleaning() {
        return checkCleaning;
    }

    public void setCheckCleaning(Boolean checkCleaning) {
        this.checkCleaning = checkCleaning;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
