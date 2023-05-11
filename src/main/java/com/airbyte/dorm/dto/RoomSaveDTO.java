package com.airbyte.dorm.dto;

import java.util.List;

public class RoomSaveDTO {
    private Integer roomNumber;
    private String description;
    private List<AccessoryDTO> accessory;
    private List<BedSaveDTO> beds;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AccessoryDTO> getAccessory() {
        return accessory;
    }

    public void setAccessory(List<AccessoryDTO> accessory) {
        this.accessory = accessory;
    }

    public List<BedSaveDTO> getBeds() {
        return beds;
    }

    public void setBeds(List<BedSaveDTO> beds) {
        this.beds = beds;
    }
}
