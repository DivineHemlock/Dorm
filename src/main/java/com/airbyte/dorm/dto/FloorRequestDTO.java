package com.airbyte.dorm.dto;

import java.util.List;

public class FloorRequestDTO extends ParentDTO {
    private List<FloorSaveDTO> floors;

    public List<FloorSaveDTO> getFloors() {
        return floors;
    }

    public void setFloors(List<FloorSaveDTO> floors) {
        this.floors = floors;
    }
}
