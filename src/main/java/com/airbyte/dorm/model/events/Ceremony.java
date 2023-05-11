package com.airbyte.dorm.model.events;

import com.airbyte.dorm.common.TimeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "ceremony")
public class Ceremony implements Serializable {
    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(500)") String title;
    private @Column(columnDefinition = "VARCHAR(4000)") String description;
    private @Column(columnDefinition = "TIMESTAMP") Date date;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return TimeConverter.convert(this.date, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    @JsonIgnore
    public Date date() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
