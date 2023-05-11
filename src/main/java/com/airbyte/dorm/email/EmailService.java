package com.airbyte.dorm.email;

import com.airbyte.dorm.dto.ForgotPasswordDTO;
import com.airbyte.dorm.manager.ManagerService;
import com.airbyte.dorm.model.people.Manager;
import com.airbyte.dorm.model.people.Supervisor;
import com.airbyte.dorm.supervisor.SupervisorService;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final ManagerService managerService;
    private final SupervisorService supervisorService;

    public EmailService(JavaMailSender emailSender, ManagerService managerService, SupervisorService supervisorService) {
        this.emailSender = emailSender;
        this.managerService = managerService;
        this.supervisorService = supervisorService;
    }

    public void sendSimpleMessage(String receiver, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("saadat.portal@gmail.com");
        message.setTo(receiver);
        message.setSubject(subject);
        message.setText(text);

        emailSender.send(message);
    }


    public void restorePassword(ForgotPasswordDTO dto) {
        String text = "Hi, %s\n" +
                "\n" +
                "We have fulfilled your request.\n" +
                "\n" +
                "Your role in the system is equal to: %s\n" +
                "\n" +
                "Your password to enter SaadatPortal is equal to: %s\n" +
                "\n" +
                "Thanks,\n" +
                "AirByte Team.";

        Manager manager = managerService.findManagerByUsername(dto.getUsername());
        if (manager != null) {
            if (dto.getEmail().equals(manager.getEmail())) {
                sendSimpleMessage(manager.getEmail(), "Forgot Password", String.format(text, manager.getFullName(), manager.getRole(), manager.getPassword()));
                return;
            }
        } else {
            Supervisor supervisor = supervisorService.findSupervisorByUsername(dto.getUsername());
            if (dto.getEmail().equals(supervisor.getEmail())) {
                sendSimpleMessage(supervisor.getEmail(),
                        "Forgot Password",
                        String.format(text, supervisor.getFullName(), supervisor.getRole(), supervisor.getPassword()));
                return;
            }
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "username with this email not found!!!");
    }


    public void sendInformation(Supervisor supervisor) {
        String text = "Hi, %s\n" +
                "\n" +
                "\n" +
                "Welcome to Saadat Portal.\n" +
                "\n" +
                "You are registered by the system manager.\n" +
                "\n" +
                "Your role in the system is supervisor.\n" +
                "\n" +
                "You can log in into the system with the following information.\n" +
                "\n" +
                "Your username: %s\n" +
                "\n" +
                "Your password: %s\n" +
                "\n" +
                "Thanks,\n" +
                "AirByte team.";

        String subject = "Register into the Saadat Portal";

        sendSimpleMessage(
                supervisor.getEmail(),
                subject,
                String.format(text, supervisor.getFullName(), supervisor.getUsername(), supervisor.getPassword())
        );
    }

    public void changePassword(String receiver, String fullName, String username, String password) {
        String text = "Hi, %s\n" +
                "\n" +
                "We have fulfilled your request.\n" +
                "\n" +
                "Your username in the system is equal to: %s\n" +
                "\n" +
                "Your new password to enter SaadatPortal is equal to: %s\n" +
                "\n" +
                "Thanks,\n" +
                "AirByte Team.";

        sendSimpleMessage(receiver, "Change Password", String.format(text, fullName, username, password));
    }
}