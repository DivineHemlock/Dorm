package com.airbyte.dorm.model.people;

import com.airbyte.dorm.model.TimePeriod;
import com.airbyte.dorm.model.enums.Gender;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "manager")
public class Manager implements Serializable {

    @Id
    private @Column(columnDefinition = "VARCHAR(50)", nullable = false) String id;
    private @Column(columnDefinition = "VARCHAR(255)") String fullName;
    private @Column(columnDefinition = "VARCHAR(50)") String profileId;
    private @Column(columnDefinition = "VARCHAR(100)") String username;
    private @Column(columnDefinition = "VARCHAR(50)") String password;
    private @Column(columnDefinition = "VARCHAR(50)") String role;
    private @Column(columnDefinition = "VARCHAR(255)") String email;

    public Manager() {
        this.role = "MANAGER";
    }

    public String getId() {
        return id;
    }

    @PrePersist
    public void setId() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
