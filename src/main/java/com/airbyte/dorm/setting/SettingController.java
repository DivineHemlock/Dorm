package com.airbyte.dorm.setting;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.SettingDTO;
import com.airbyte.dorm.model.Setting;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/setting")
@CrossOrigin("*")
public class SettingController extends ParentController<Setting, SettingService, SettingDTO> {

    public SettingController(SettingService service) {
        super(service);
    }
}
