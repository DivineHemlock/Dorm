package com.airbyte.dorm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

@Table
@Entity
public class LogHistory implements Serializable, Comparator<LogHistory> {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(100)") String url;
    private @Column(columnDefinition = "VARCHAR(100)") String doer;
    private @Column(columnDefinition = "VARCHAR(20)") String action;
    private @Column(columnDefinition = "VARCHAR(50)") String date;
    private @Column(columnDefinition = "VARCHAR(50)") String hour;
    private @Column(columnDefinition = "VARCHAR(255)") String category;
    private @JsonIgnore
    @Column(columnDefinition = "TIMESTAMP") Date registerDate;

    public LogHistory() {
        this.registerDate = Date.from(Instant.now());
    }

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDoer() {
        return doer;
    }

    public void setDoer(String doer) {
        this.doer = doer;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public int compare(LogHistory o1, LogHistory o2) {
        return (int) (o1.getRegisterDate().getTime() - o2.getRegisterDate().getTime());
    }
}
