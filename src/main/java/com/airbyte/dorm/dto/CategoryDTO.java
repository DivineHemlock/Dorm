package com.airbyte.dorm.dto;

import javax.validation.constraints.NotBlank;

public class CategoryDTO extends ParentDTO {
    private @NotBlank String name;
    private @NotBlank String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
