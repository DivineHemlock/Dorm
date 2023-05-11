package com.airbyte.dorm.changepassword;

import com.airbyte.dorm.dto.ChangePasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/changePassword")
@CrossOrigin("*")
public class ChangePasswordController {
    private final ChangePasswordService service;

    public ChangePasswordController(ChangePasswordService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO dto) {
        service.changePassword(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
