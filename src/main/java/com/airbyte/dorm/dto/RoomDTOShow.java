package com.airbyte.dorm.dto;

import java.io.Serializable;

public class RoomDTOShow implements Serializable {
    private String concatName;
    private String id;


    public String getConcatName() {
        return concatName;
    }

    public void setConcatName(String concatName) {
        this.concatName = concatName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
