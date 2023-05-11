package com.airbyte.dorm.dto;


import com.airbyte.dorm.annotations.MobileNumber;
import com.airbyte.dorm.annotations.NationalCode;
import com.airbyte.dorm.annotations.Number;
import com.airbyte.dorm.annotations.TelephoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class PersonDTO extends ParentDTO {
    private String residenceType;
    private String accommodationType;
    private String characteristicId;
    private List<FileDTO> files;

    public String getResidenceType() {
        return residenceType;
    }

    public void setResidenceType(String residenceType) {
        this.residenceType = residenceType;
    }

    public String getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(String accommodationType) {
        this.accommodationType = accommodationType;
    }

    public String getCharacteristicId() {
        return characteristicId;
    }

    public void setCharacteristicId(String characteristicId) {
        this.characteristicId = characteristicId;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }
}
