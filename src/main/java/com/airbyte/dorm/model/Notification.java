package com.airbyte.dorm.model;

import com.airbyte.dorm.common.TimeConverter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    private @Column (columnDefinition = "VARCHAR(50)" , nullable = false) String id;
    private @Column (columnDefinition = "VARCHAR(100)") String eventName;
    private @Column (columnDefinition = "VARCHAR(1000)") String eventDescription;
    private @Column(columnDefinition = "TIMESTAMP") Date date;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-","");
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Date date() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDate() {
        return TimeConverter.convert(this.date, TimeConverter.DEFAULT_PATTERN_FORMAT);
    }
}
