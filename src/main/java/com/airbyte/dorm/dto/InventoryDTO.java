package com.airbyte.dorm.dto;

import java.util.List;

public class InventoryDTO extends ParentDTO {
    private List<AccessoryDTO> accessories;
    private String category;
    private String accessoryType;
    private String name;

    public List<AccessoryDTO> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<AccessoryDTO> accessories) {
        this.accessories = accessories;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(String accessoryType) {
        this.accessoryType = accessoryType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
