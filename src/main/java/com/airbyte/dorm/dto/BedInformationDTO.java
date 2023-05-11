package com.airbyte.dorm.dto;

public class BedInformationDTO extends ParentDTO {
    private String bedName;
    private String personId;
    private String personName;
    private String personNationalCode;
    private Boolean checked;

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonNationalCode() {
        return personNationalCode;
    }

    public void setPersonNationalCode(String personNationalCode) {
        this.personNationalCode = personNationalCode;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
