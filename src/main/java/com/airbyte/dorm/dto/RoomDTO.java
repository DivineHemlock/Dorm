package com.airbyte.dorm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RoomDTO extends ParentDTO {
    private @NotNull Integer number;
    private @NotNull Boolean empty;
    private @NotBlank String unitId;
    private String description;
    private List<AccessoryDTO> accessory;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public List<AccessoryDTO> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<AccessoryDTO> accessory) {
        this.accessory = accessory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
