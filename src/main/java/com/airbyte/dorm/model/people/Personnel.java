package com.airbyte.dorm.model.people;

import com.airbyte.dorm.common.FileConverter;
import com.airbyte.dorm.model.*;
import com.airbyte.dorm.model.enums.Gender;
import com.airbyte.dorm.model.enums.ResidenceType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "personnel")
public class Personnel implements Serializable {

    private @Id
    @Column(columnDefinition = "VARCHAR(50)") String id;
    private @OneToMany(mappedBy = "personnel", fetch = FetchType.LAZY, cascade = CascadeType.ALL) List<Task> tasks;
    private @Column(columnDefinition = "VARCHAR(50)") String characteristicId;
    private @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(50)") Gender gender;
    private @Convert(converter = FileConverter.class)
    @Column(columnDefinition = "VARCHAR(1000)") Map<String, String> files;
    private @Enumerated(EnumType.STRING)
    @Column ResidenceType residenceType;
    private @Column(columnDefinition = "VARCHAR(255)") String type;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getCharacteristicId() {
        return characteristicId;
    }

    public void setCharacteristicId(String characteristicId) {
        this.characteristicId = characteristicId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public ResidenceType getResidenceType() {
        return residenceType;
    }

    public void setResidenceType(ResidenceType residenceType) {
        this.residenceType = residenceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
}



