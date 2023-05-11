package com.airbyte.dorm.model;

import com.airbyte.dorm.model.events.CameraHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "unit")
@Entity
public class Unit implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "SMALLINT CHECK(number >= 0)", nullable = false) Integer number;
    private @JsonIgnore
    @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Room> rooms = new ArrayList<>();
    private @Column(columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false) Boolean empty;
    private @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "floor_id", nullable = false, referencedColumnName = "id") Floor floor;
    private @OneToMany(mappedBy = "unit", fetch = FetchType.LAZY, cascade = CascadeType.ALL) List<Accessory> accessories;

    public Unit() {
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

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @JsonProperty("floor")
    public String getFloorId() {
        return this.floor.getId();
    }

    public Unit name(Integer number) {
        this.number = number;
        return this;
    }

    public Unit floor(Floor floor) {
        this.floor = floor;
        return this;
    }

    public List<Accessory> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<Accessory> accessories) {
        this.accessories = accessories;
    }
}
