package com.airbyte.dorm.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class BedDTO extends ParentDTO {
    private @NotBlank String name;
    private @NotNull Boolean empty;
    private @NotBlank String roomId;
    private String personId;

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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
