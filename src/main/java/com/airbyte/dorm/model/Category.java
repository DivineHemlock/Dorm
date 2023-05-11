package com.airbyte.dorm.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table
public class Category implements Serializable {

    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(255)", nullable = false) String name;
    private @Column(columnDefinition = "VARCHAR(255)", nullable = false) String type;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
