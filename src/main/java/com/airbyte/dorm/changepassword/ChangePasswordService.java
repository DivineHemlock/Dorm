package com.airbyte.dorm.changepassword;

import com.airbyte.dorm.dto.ChangePasswordDTO;
import com.airbyte.dorm.dto.SupervisorDTO;
import com.airbyte.dorm.email.EmailService;
import com.airbyte.dorm.manager.ManagerRepository;
import com.airbyte.dorm.manager.ManagerService;
import com.airbyte.dorm.model.people.Manager;
import com.airbyte.dorm.model.people.Supervisor;
import com.airbyte.dorm.supervisor.SupervisorRepository;
import com.airbyte.dorm.supervisor.SupervisorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ChangePasswordService {
    private final SupervisorService supervisorService;
    private final SupervisorRepository supervisorRepository;
    private final ManagerService managerService;
    private final ManagerRepository managerRepository;
    private final EmailService emailService;

    public ChangePasswordService(SupervisorService supervisorService, SupervisorRepository supervisorRepository, ManagerService managerService, ManagerRepository managerRepository, EmailService emailService) {
        this.supervisorService = supervisorService;
        this.supervisorRepository = supervisorRepository;
        this.managerService = managerService;
        this.managerRepository = managerRepository;
        this.emailService = emailService;
    }

    public void changePassword(ChangePasswordDTO dto) {
        if (dto.getRole().equals("SUPERVISOR")) {

            SupervisorDTO supervisorDTO = new SupervisorDTO();
            supervisorDTO.setUsername(dto.getUsername());
            List<Supervisor> supervisorList = supervisorService.getWithSearch(supervisorDTO);

            if (supervisorList != null && !supervisorList.isEmpty()) {

                Supervisor supervisor = supervisorList.get(0);
                if (supervisor.getPassword().equals(dto.getOldPassword())) {
                    supervisor.setPassword(dto.getNewPassword());
                    supervisorRepository.save(supervisor);
                    emailService.changePassword(supervisor.getEmail(), supervisor.getFullName(), supervisor.getUsername(), supervisor.getPassword());
                } else if (dto.getOldPassword() == null && dto.getNewPassword() == null) {
                    supervisor.setEmail(dto.getEmail());
                    supervisorRepository.save(supervisor);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "username not found!!");
                }

            }
        } else if (dto.getRole().equals("MANAGER")) {
            Manager manager = managerService.getOne("1");

            if (manager.getPassword().equals(dto.getOldPassword())) {
                manager.setPassword(dto.getNewPassword());
                managerRepository.save(manager);
                emailService.changePassword(manager.getEmail(), manager.getFullName(), manager.getUsername(), manager.getPassword());

            } else if (dto.getOldPassword() == null && dto.getNewPassword() == null) {
                manager.setEmail(dto.getEmail());
                managerRepository.save(manager);

            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "username not found!!");
            }
        }
    }
}
