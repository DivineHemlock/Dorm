package com.airbyte.dorm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "room")
@Entity
public class Room implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "SMALLINT CHECK(number >= 0)", nullable = false) Integer number;
    private @Column(columnDefinition = "VARCHAR(255)") String description;
    private @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Bed> beds = new ArrayList<>();
    private @Column(columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false) Boolean empty;
    private @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unit_id", referencedColumnName = "id", nullable = false) Unit unit;
    private @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL) List<Accessory> accessories;

    public Room() {
        this.empty = true;
    }


    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void setBeds(List<Bed> beds) {
        this.beds = beds;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("unit")
    public String getUnitId() {
        return this.unit.getId();
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }

    @JsonProperty("concatName")
    public String getConcatName() {
        return this.unit.getNumber() + "-" + this.getNumber();
    }

    public Room number(Integer number) {
        this.number = number;
        return this;
    }

    public Room unit(Unit unit) {
        this.unit = unit;
        return this;
    }
}
