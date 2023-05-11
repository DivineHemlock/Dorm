package com.airbyte.dorm.manager;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.ManagerDTO;
import com.airbyte.dorm.model.people.Manager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ManagerService extends ParentService<Manager, ManagerRepository, ManagerDTO> implements UserDetailsService {

    private final NoOpPasswordEncoder passwordEncoder;

    public ManagerService(ManagerRepository repository) {
        super(repository);
        this.passwordEncoder = (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Override
    public Manager updateModelFromDto(Manager model, ManagerDTO dto) {
        model.setFullName(dto.getFullName() != null ? dto.getFullName() : model.getFullName());
        model.setPassword(dto.getPassword() != null ? dto.getPassword() : model.getPassword());
        model.setUsername(dto.getUsername() != null ? dto.getUsername() : model.getUsername());
        model.setEmail(dto.getEmail() != null ? dto.getEmail() : model.getEmail());
        model.setRole(dto.getRole() != null ? dto.getRole() : model.getRole());
        model.setProfileId(dto.getProfileId() != null ? dto.getProfileId() : model.getProfileId());
        return model;
    }

    @Override
    public Manager convertDTO(ManagerDTO dto) {
        Manager manager = new Manager();
        manager.setFullName(dto.getFullName());
        manager.setPassword(dto.getPassword());
        manager.setUsername(dto.getUsername());
        manager.setEmail(dto.getEmail());
        manager.setProfileId(dto.getProfileId());
        return manager;
    }

    @Override
    public List<Manager> getWithSearch(ManagerDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Manager> criteriaBuilderQuery = criteriaBuilder.createQuery(Manager.class);

        Root<Manager> managerRoot = criteriaBuilderQuery.from(Manager.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getUsername() != null) {
            predicates.add(criteriaBuilder.equal(managerRoot.get("username"), search.getUsername()));
        }
        if (search.getEmail() != null) {
            predicates.add(criteriaBuilder.equal(managerRoot.get("email"), search.getEmail()));
        }

        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaBuilderQuery).getResultList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Manager manager = this.findManagerByUsername(username);
        if (manager == null) {
            log.error("no such user is present in the DB !");
            throw new UsernameNotFoundException("no such user is present in the DB !");
        } else {
            log.info("user {} found in the DB", username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(manager.getRole()));
        return new User(manager.getUsername(), manager.getPassword(), authorities);
    }

    public Manager findManagerByUsername(String username) {
        ManagerDTO dto = new ManagerDTO();
        dto.setUsername(username);
        List<Manager> manager = getWithSearch(dto);
        if (manager == null || manager.isEmpty()) {
            return null;
        }
        return manager.get(0);
    }

    public Manager getByEmail(String email) {
        ManagerDTO dto = new ManagerDTO();
        dto.setEmail(email);
        List<Manager> manager = getWithSearch(dto);
        if (manager == null || manager.isEmpty()) {
            return null;
        }
        return manager.get(0);
    }
}
