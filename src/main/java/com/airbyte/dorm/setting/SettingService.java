package com.airbyte.dorm.setting;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.SettingDTO;
import com.airbyte.dorm.model.Setting;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class SettingService extends ParentService<Setting, SettingRepository, SettingDTO> {

    public SettingService(SettingRepository repository) {
        super(repository);
    }

    @Override
    public Setting updateModelFromDto(Setting model, SettingDTO dto) {
        if (dto.getPhotos() != null) {
            Map<String, String> photos = new TreeMap<>();
            dto.getPhotos().forEach(photo -> photos.put(photo.getName(), photo.getFileId()));
            model.setPhotos(photos);
        } else {
            model.setPhotos(model.getPhotos());
        }

        if (dto.getSlider() != null) {
            Map<String, String> sliders = new TreeMap<>();
            dto.getSlider().forEach(slider -> sliders.put(slider.getName(), slider.getFileId()));
            model.setSlider(sliders);
        } else {
            model.setSlider(model.getSlider());
        }

        return model;
    }

    @Override
    public Setting convertDTO(SettingDTO dto) {
        Setting setting = new Setting();
        if (dto.getPhotos() != null) {
            Map<String, String> photos = new TreeMap<>();
            dto.getPhotos().forEach(photo -> photos.put(photo.getName(), photo.getFileId()));
            setting.setPhotos(photos);
        }

        if (dto.getSlider() != null) {
            Map<String, String> sliders = new TreeMap<>();
            dto.getSlider().forEach(slider -> sliders.put(slider.getName(), slider.getFileId()));
            setting.setSlider(sliders);
        }

        return setting;
    }

    @Override
    public List<Setting> getWithSearch(SettingDTO search) {
        return null;
    }
}
