package com.airbyte.dorm.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class FloorDTO extends ParentDTO {
    private @NotBlank String name;
    private @NotNull Boolean empty;
    private List<AccessoryDTO> accessory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public List<AccessoryDTO> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<AccessoryDTO> accessory) {
        this.accessory = accessory;
    }
}
