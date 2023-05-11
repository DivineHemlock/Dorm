package com.airbyte.dorm.model;

import com.airbyte.dorm.common.TimeConverter;
import com.airbyte.dorm.model.enums.Priority;
import com.airbyte.dorm.model.enums.Status;
import com.airbyte.dorm.model.people.Personnel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "task")
public class Task implements Serializable {

    @Id
    private @Column (columnDefinition = "VARCHAR(50)" , nullable = false) String id;
    private @Column (columnDefinition = "VARCHAR(1000)") String name;
    private @Column (columnDefinition = "VARCHAR(4000)") String description;
    private @Column(columnDefinition = "TIMESTAMP") Date dueDate;
    private @Column Double timeLog;
    private @Enumerated(EnumType.STRING) @Column (columnDefinition = "VARCHAR(50)") Priority priority;
    private @Enumerated(EnumType.STRING) @Column (columnDefinition = "VARCHAR(50)") Status status;
    private @JoinColumn @ManyToOne (fetch = FetchType.LAZY) @JsonIgnore Personnel personnel;
    private @Column (columnDefinition = "VARCHAR(255)") String fullName;


    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-","");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTimeLog() {
        return timeLog;
    }

    public void setTimeLog(Double timeLog) {
        this.timeLog = timeLog;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public String getDueDate() {
        return TimeConverter.convert(this.dueDate, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date dueDate() {
        return this.dueDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty("personnelId")
    public String getPersonnelId () {
        if (this.getPersonnel() != null) {
            return this.getPersonnel().getId();
        }
        return null;
    }
}
