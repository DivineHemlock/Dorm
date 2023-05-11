package com.airbyte.dorm.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "related_object")
public class RelatedObject {

    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column String name;
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String parentId;
    private @Column(columnDefinition = "VARCHAR(50)") String childId;
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String type;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
