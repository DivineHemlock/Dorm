package com.airbyte.dorm.dto;

import java.nio.file.LinkOption;
import java.util.List;

public class PhoneBookDTO extends ParentDTO{

    private String name;
    private List<String> telephoneNumbers;
    private List<String> mobileNumbers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTelephoneNumbers() {
        return telephoneNumbers;
    }

    public void setTelephoneNumbers(List<String> telephoneNumbers) {
        this.telephoneNumbers = telephoneNumbers;
    }

    public List<String> getMobileNumbers() {
        return mobileNumbers;
    }

    public void setMobileNumbers(List<String> mobileNumbers) {
        this.mobileNumbers = mobileNumbers;
    }
}
