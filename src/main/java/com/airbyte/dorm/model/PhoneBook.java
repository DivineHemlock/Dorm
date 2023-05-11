package com.airbyte.dorm.model;

import com.airbyte.dorm.common.StringListConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "phone_book")
public class PhoneBook implements Serializable {

    @Id
    private @Column (columnDefinition = "VARCHAR(50)" , nullable = false) String id;
    private @Column (columnDefinition = "VARCHAR(100)") String name;
    private @Column @Convert(converter = StringListConverter.class) List<String> telephoneNumbers;
    private @Column @Convert(converter = StringListConverter.class) List<String> mobileNumbers;


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
