package com.airbyte.dorm.dto;

import java.util.List;

public class PresentAbsentDTO extends ParentDTO {
    private Integer roomNumber;
    private List<BedInformationDTO> information;

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<BedInformationDTO> getInformation() {
        return information;
    }

    public void setInformation(List<BedInformationDTO> information) {
        this.information = information;
    }
}
