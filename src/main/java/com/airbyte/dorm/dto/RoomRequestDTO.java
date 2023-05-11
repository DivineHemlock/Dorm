package com.airbyte.dorm.dto;

import java.util.List;

public class RoomRequestDTO {
    private String unitId;
    private List<RoomSaveDTO> rooms;

    public List<RoomSaveDTO> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomSaveDTO> rooms) {
        this.rooms = rooms;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
