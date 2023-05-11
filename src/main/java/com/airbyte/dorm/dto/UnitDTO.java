package com.airbyte.dorm.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class UnitDTO extends ParentDTO {
    private @NotNull Boolean empty;
    private @NotNull Integer number;
    private @NotBlank String floorId;
    private List<AccessoryDTO> accessory;

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public List<AccessoryDTO> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<AccessoryDTO> accessory) {
        this.accessory = accessory;
    }
}
