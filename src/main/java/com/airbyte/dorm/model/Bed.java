package com.airbyte.dorm.model;

import com.airbyte.dorm.model.people.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Table(name = "bed")
@Entity
public class Bed implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(50)") String name;
    private @JsonIgnore @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "person_id") Person person;
    private @Column(columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false) Boolean empty;
    private @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "id") Room room;

    public Bed() {
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @JsonProperty("room")
    public String getRoomId() {
        return this.room.getId();
    }

    @JsonProperty("person")
    public String getPersonId(){
        if (this.person != null) {
            return this.person.getId();
        }
        return null;
    }

    public Bed name(String name) {
        this.name = name;
        return this;
    }

    public Bed room(Room room) {
        this.room = room;
        return this;
    }
}
