package com.airbyte.dorm.model;

import com.airbyte.dorm.common.FileConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Entity
@Table
public class Setting implements Serializable {
    private @Id
    @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;

    private @Convert(converter = FileConverter.class)
    @Column(columnDefinition = "VARCHAR(1000)") Map<String, String> photos;

    private @Convert(converter = FileConverter.class)
    @Column(columnDefinition = "VARCHAR(1000)") Map<String, String> slider;

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public Map<String, String> getPhotos() {
        return photos;
    }

    public void setPhotos(Map<String, String> photos) {
        this.photos = photos;
    }

    public Map<String, String> getSlider() {
        return slider;
    }

    public void setSlider(Map<String, String> slider) {
        this.slider = slider;
    }
}
