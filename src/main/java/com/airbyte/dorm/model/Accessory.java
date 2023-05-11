package com.airbyte.dorm.model;

import com.airbyte.dorm.model.enums.AccessoryType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "accessory")
@Entity
public class Accessory implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(255)", nullable = false) String name;
    private @Column(columnDefinition = "NUMERIC(5, 0)") BigDecimal count;
    private @Column(columnDefinition = "VARCHAR(5000)") String description;
    private @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id") Room room;
    private @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id") Unit unit;
    private @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id") Inventory inventory;
    private @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id") Floor floor;

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

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @JsonIgnore
    public String getFloorId() {
        if (this.floor != null) {
            return this.floor.getId();
        }
        return null;
    }
    @JsonIgnore
    public String getUnitId() {
        if (this.unit != null) {
            return this.unit.getId();
        }
        return null;
    }
    @JsonIgnore
    public String getInventoryId() {
        if (this.inventory != null) {
            return this.inventory.getId();
        }
        return null;
    }
    @JsonIgnore
    public String getRoomId() {
        if (this.room != null) {
            return this.room.getId();
        }
        return null;
    }
}
