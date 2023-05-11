package com.airbyte.dorm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "floor")
@Entity
public class Floor implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(255)") String name;
    private @OneToMany(mappedBy = "floor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Unit> units = new ArrayList<>();
    private @Column(columnDefinition = "BOOLEAN DEFAULT TRUE", nullable = false) Boolean empty;
    private @OneToMany(mappedBy = "floor", fetch = FetchType.LAZY, cascade = CascadeType.ALL) List<Accessory> accessories;

    public Floor() {
        this.empty = true;
    }

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

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Floor name(String name) {
        this.name = name;
        return this;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }
}
