package com.airbyte.dorm.model.events;

import com.airbyte.dorm.common.TimeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "camera_history")
public class CameraHistory implements Serializable {
    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(100)") String title;
    private @Column(columnDefinition = "VARCHAR(4000)") String description;
    private @Column(columnDefinition = "TIMESTAMP") Date date;
    private @Column(columnDefinition = "VARCHAR(20)") String unit;

    private  @Column(columnDefinition = "VARCHAR(255)") String supervisor;
    private  @Column(columnDefinition = "VARCHAR(255)") String assignee;

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

    public void setDate(Date date) {
        this.date = date;
    }

    @JsonIgnore
    public Date date() {
        return date;
    }

    public String getDate() {
        return TimeConverter.convert(this.date, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
