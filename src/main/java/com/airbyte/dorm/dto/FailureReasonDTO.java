package com.airbyte.dorm.dto;

import com.airbyte.dorm.annotations.Text;

import javax.validation.constraints.NotBlank;

public class FailureReasonDTO extends ParentDTO {
    private @NotBlank String name;
    private @NotBlank String reason;
    private @NotBlank String type;

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
}
