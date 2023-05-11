package com.airbyte.dorm.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class ResponseFile {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(50)") String fileId;
    private @Column(columnDefinition = "VARCHAR(255)") String name;
    private @Column(columnDefinition = "VARCHAR(255)") String type;
    private @Column(columnDefinition = "VARCHAR(50)") String parentId;
    private @Column(columnDefinition = "VARCHAR(50)") String parentType;
    private @Column(columnDefinition = "VARCHAR(50)") String originalName;
    private @Column(columnDefinition = "VARCHAR(50)") String fullType;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFullType() {
        return fullType;
    }

    public void setFullType(String fullType) {
        this.fullType = fullType;
    }
}
