package com.airbyte.dorm.telephonehistory;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.TelephoneHistoryDTO;
import com.airbyte.dorm.model.events.TelephoneHistory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/telephoneHistory")
@CrossOrigin("*")
public class TelephoneHistoryController extends ParentController<TelephoneHistory, TelephoneHistoryService, TelephoneHistoryDTO> {
    public TelephoneHistoryController(TelephoneHistoryService service) {
        super(service);
    }
}
