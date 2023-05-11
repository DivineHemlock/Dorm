package com.airbyte.dorm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AccessoryDTO extends ParentDTO {
    private @NotBlank String name;
    private @NotNull Long count;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
