package com.airbyte.dorm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RequestDTO extends ParentDTO {
    private Boolean checked;
    private @NotBlank String dateOfRegistration;
    private @NotBlank String description;
    private @NotBlank String type;
    private @NotBlank String reason;
    private @NotBlank String name;
    private String supervisorId;
    private String failureReasonId;
    private @NotBlank String assignee;
    private String status;
    private String statusDescription;


    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(String supervisorId) {
        this.supervisorId = supervisorId;
    }

    public String getFailureReasonId() {
        return failureReasonId;
    }

    public void setFailureReasonId(String failureReasonId) {
        this.failureReasonId = failureReasonId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
