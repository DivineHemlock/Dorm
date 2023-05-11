package com.airbyte.dorm.dto;

import java.util.List;

public class UnitSaveDTO extends ParentDTO {
    private Integer number;
    private List<AccessoryDTO> accessory;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<AccessoryDTO> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<AccessoryDTO> accessory) {
        this.accessory = accessory;
    }
}
