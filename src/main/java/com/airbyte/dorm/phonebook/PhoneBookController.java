package com.airbyte.dorm.phonebook;

import com.airbyte.dorm.common.ParentController;
import com.airbyte.dorm.dto.PhoneBookDTO;
import com.airbyte.dorm.model.PhoneBook;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/supervisor/phoneBook")
@CrossOrigin("*")
public class PhoneBookController extends ParentController<PhoneBook, PhoneBookService, PhoneBookDTO> {
    public PhoneBookController(PhoneBookService service) {
        super(service);
    }
}
