package com.airbyte.dorm.model;

import com.airbyte.dorm.common.StringListConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Table(name = "permanent_resident")
@Entity
public class PermanentResident implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;

    private @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name = "char_id", referencedColumnName = "id") Characteristic characteristic;

    private @Embedded TimePeriod validFor;
    private @Column(columnDefinition = "VARCHAR(50)") String bedId;

    private @Convert(converter = StringListConverter.class) @Column(columnDefinition = "VARCHAR(4000)") List<String> certificatePath;
    private @Convert(converter = StringListConverter.class) @Column(columnDefinition = "VARCHAR(2000)") List<String> nationalCardPath;
    private @Convert(converter = StringListConverter.class) @Column(columnDefinition = "VARCHAR(1000)") List<String> picturePath;
    private @Convert(converter = StringListConverter.class) @Column(columnDefinition = "VARCHAR(1000)") List<String> universityRegistrationPath;
    private @Convert(converter = StringListConverter.class) @Column(columnDefinition = "VARCHAR(1000)") List<String> signPath;
    private @Convert(converter = StringListConverter.class) @Column(columnDefinition = "VARCHAR(1000)") List<String> contractFormPath;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public TimePeriod getValidFor() {
        return validFor;
    }

    public void setValidFor(TimePeriod validFor) {
        this.validFor = validFor;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public List<String> getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(List<String> certificatePath) {
        this.certificatePath = certificatePath;
    }

    public List<String> getNationalCardPath() {
        return nationalCardPath;
    }

    public void setNationalCardPath(List<String> nationalCardPath) {
        this.nationalCardPath = nationalCardPath;
    }

    public List<String> getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(List<String> picturePath) {
        this.picturePath = picturePath;
    }

    public List<String> getUniversityRegistrationPath() {
        return universityRegistrationPath;
    }

    public void setUniversityRegistrationPath(List<String> universityRegistrationPath) {
        this.universityRegistrationPath = universityRegistrationPath;
    }

    public List<String> getSignPath() {
        return signPath;
    }

    public void setSignPath(List<String> signPath) {
        this.signPath = signPath;
    }

    public List<String> getContractFormPath() {
        return contractFormPath;
    }

    public void setContractFormPath(List<String> contractFormPath) {
        this.contractFormPath = contractFormPath;
    }
}
