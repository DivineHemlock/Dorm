package com.airbyte.dorm.dto;

public class RelatedObjectDTO extends ParentDTO {

    private String name;
    private String childId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
