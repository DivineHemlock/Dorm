package com.airbyte.dorm.supervisor;

import com.airbyte.dorm.common.ParentService;
import com.airbyte.dorm.dto.SupervisorDTO;
import com.airbyte.dorm.model.people.Supervisor;
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
import java.util.*;

@Service
@Slf4j
public class SupervisorService extends ParentService <Supervisor , SupervisorRepository , SupervisorDTO>  implements UserDetailsService {

    private final NoOpPasswordEncoder bCryptPasswordEncoder;

    public SupervisorService(SupervisorRepository repository) {
        super(repository);
        this.bCryptPasswordEncoder = (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }

    @Override
    public Supervisor updateModelFromDto(Supervisor model, SupervisorDTO dto) {
        model.setFullName(dto.getFullName() != null ? dto.getFullName() : model.getFullName());
        model.setPassword(dto.getPassword() != null ? dto.getPassword() : model.getPassword());
        model.setUsername(dto.getUsername() != null ? dto.getUsername() : model.getUsername());
        model.setEmail(dto.getEmail() != null ? dto.getEmail() : model.getEmail());
        model.setProfileId(dto.getProfileId() != null ? dto.getProfileId() : model.getProfileId());
        return model;
    }

    @Override
    public Supervisor convertDTO(SupervisorDTO dto) {
        Supervisor supervisor = new Supervisor();
        supervisor.setFullName(dto.getFullName());
        supervisor.setPassword(dto.getPassword());
        supervisor.setUsername(dto.getUsername());
        supervisor.setEmail(dto.getEmail());
        supervisor.setProfileId(dto.getProfileId());
        return supervisor;
    }

    @Override
    public List<Supervisor> getWithSearch(SupervisorDTO search) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Supervisor> criteriaBuilderQuery = criteriaBuilder.createQuery(Supervisor.class);

        Root<Supervisor> supervisorRoot = criteriaBuilderQuery.from(Supervisor.class);
        List<Predicate> predicates = new ArrayList<>();

        if (search.getId() != null) {
            predicates.add(criteriaBuilder.equal(supervisorRoot.get("id"), search.getId()));
        }
        if (search.getUsername() != null) {
            predicates.add(criteriaBuilder.equal(supervisorRoot.get("username"), search.getUsername()));
        }
        if (search.getEmail() != null) {
            predicates.add(criteriaBuilder.equal(supervisorRoot.get("email"), search.getEmail()));
        }
        criteriaBuilderQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaBuilderQuery).getResultList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Supervisor supervisor = this.findSupervisorByUsername(username);
        if (supervisor == null) {
            log.error("no such user is present in the DB !");
            throw new UsernameNotFoundException("no such user is present in the DB !");
        }
        else {
            log.info("user {} found in the DB" , username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(supervisor.getRole()));
        return new User(supervisor.getUsername() , supervisor.getPassword(), authorities);
    }

    public Supervisor findSupervisorByUsername(String username) {
        SupervisorDTO dto = new SupervisorDTO();
        dto.setUsername(username);
        List<Supervisor> supervisor = getWithSearch(dto);
        if (supervisor == null || supervisor.isEmpty()) {
            return null;
        }
        return supervisor.get(0);
    }

    public Supervisor getByEmail(String email) {
        SupervisorDTO dto = new SupervisorDTO();
        dto.setEmail(email);
        List<Supervisor> supervisor = getWithSearch(dto);
        if (supervisor == null || supervisor.isEmpty()) {
            return null;
        }
        return supervisor.get(0);
    }
}
