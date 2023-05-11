package com.airbyte.dorm.dto;

import javax.validation.constraints.NotBlank;

public class FileDTO extends ParentDTO {
    private @NotBlank String name;
    private @NotBlank String fileId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
