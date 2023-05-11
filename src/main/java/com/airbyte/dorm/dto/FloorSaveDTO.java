package com.airbyte.dorm.dto;

import java.util.List;

public class FloorSaveDTO extends ParentDTO {
    private String floorName;
    private List<UnitSaveDTO> units;


    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public List<UnitSaveDTO> getUnits() {
        return units;
    }

    public void setUnits(List<UnitSaveDTO> units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return "FloorSaveDTO{" +
                "floorName='" + floorName + '\'' +
                ", unit=" + units +
                '}';
    }
}
