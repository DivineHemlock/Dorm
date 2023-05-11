package com.airbyte.dorm.model;

import com.airbyte.dorm.model.enums.AccessoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {
    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL) List<Accessory> accessories;
    private @Column(columnDefinition = "VARCHAR(100)") String accessoryType;
    private @Column(columnDefinition = "VARCHAR(255)") String category;
    private @JsonIgnore
    @Column(columnDefinition = "VARCHAR(255)") String name;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(String accessoryType) {
        this.accessoryType = accessoryType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
