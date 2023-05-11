package com.airbyte.dorm.model.people;

import com.airbyte.dorm.common.FileConverter;
import com.airbyte.dorm.common.JsonConverter;
import com.airbyte.dorm.model.*;
import com.airbyte.dorm.model.enums.AccommodationType;
import com.airbyte.dorm.model.enums.Gender;
import com.airbyte.dorm.model.enums.RegistrationType;
import com.airbyte.dorm.model.enums.ResidenceType;
import com.airbyte.dorm.model.events.CameraHistory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "person")
public class Person implements Serializable {
    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "person")
    Bed bed;

    private @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)") ResidenceType residenceType;

    private @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)") AccommodationType accommodationType;
    private @Column(columnDefinition = "VARCHAR(50)") String characteristicId;
    private @Convert(converter = FileConverter.class)
    @Column(columnDefinition = "VARCHAR(1000)") Map<String, String> files;
    private @OneToMany(mappedBy = "person", cascade = CascadeType.ALL) List<Record> record = new ArrayList<>();

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public Bed getBed() {
        return bed;
    }

    public void setBed(Bed bed) {
        this.bed = bed;
    }

    public ResidenceType getResidenceType() {
        return residenceType;
    }

    public void setResidenceType(ResidenceType residenceType) {
        this.residenceType = residenceType;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public String getCharacteristicId() {
        return characteristicId;
    }

    public void setCharacteristicId(String characteristicId) {
        this.characteristicId = characteristicId;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public List<Record> getRecord() {
        return record;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }

    @JsonProperty("bed")
    public String getBedId() {
        if (this.bed != null) {
            return this.bed.getId();
        }
        return null;
    }
}
