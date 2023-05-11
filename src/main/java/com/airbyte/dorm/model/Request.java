package com.airbyte.dorm.model;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table(name = "request")
@Entity
public class Request implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(255)") String name;
    private @Column(columnDefinition = "VARCHAR(4000)") String reason;
    private @Column(columnDefinition = "VARCHAR(100)") String type;
    private @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "failure_reason_id", referencedColumnName = "id") FailureReason failureReason;
    private @Column(columnDefinition = "VARCHAR(4000)") String description;
    private @Column(columnDefinition = "TIMESTAMP") Date dateOfRegistration;
    private @Column(columnDefinition = "BOOLEAN") Boolean isChecked;
    private @Column(columnDefinition = "VARCHAR(255)") String assignee;
    private @Column(columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING) Status status;
    private @Column(columnDefinition = "VARCHAR(4000)") String statusDescription;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateOfRegistration() {
        return TimeConverter.convert(this.dateOfRegistration, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public void setDateOfRegistration(Date dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public Date dateOfRegistration() {
        return this.dateOfRegistration;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @JsonProperty("failureReason")
    public String getFailureReasonId() {
        if (failureReason != null) {
            return this.getFailureReason().getId();
        }
        return null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
