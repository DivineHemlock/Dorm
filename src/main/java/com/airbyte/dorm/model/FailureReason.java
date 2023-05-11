package com.airbyte.dorm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Table(name = "failure_reason")
@Entity
public class FailureReason implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(255)") String name;
    private @Column(columnDefinition = "VARCHAR(4000)") String reason;
    private @Column(columnDefinition = "VARCHAR(100)") String type;
    private @JsonIgnore
    @OneToOne(mappedBy = "failureReason") Request request;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @JsonProperty("requestId")
    public String getRequestId() {
        if (this.request != null) {
            return this.getRequest().getId();
        }
        return null;
    }
}
