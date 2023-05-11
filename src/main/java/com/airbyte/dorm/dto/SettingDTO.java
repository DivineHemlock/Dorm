package com.airbyte.dorm.dto;

import java.util.List;

public class SettingDTO extends ParentDTO {

    private List<FileDTO> photos;
    private List<FileDTO> slider;


    public List<FileDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(List<FileDTO> photos) {
        this.photos = photos;
    }

    public List<FileDTO> getSlider() {
        return slider;
    }

    public void setSlider(List<FileDTO> slider) {
        this.slider = slider;
    }
}
