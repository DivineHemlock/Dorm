package com.airbyte.dorm.email;

import com.airbyte.dorm.dto.ForgotPasswordDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping("/forgot/password")
    public ResponseEntity<Void> forgotPassword(HttpServletResponse response, Authentication authentication, @RequestBody ForgotPasswordDTO dto) {
        service.restorePassword(dto);
        response.setHeader("role", authentication.getAuthorities().toString().replace("[", "").replace("]", ""));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
