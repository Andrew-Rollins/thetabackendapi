package com.tx.thetabackendapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    
    private String username;
    private String email;
    
    @Column(name = "password_hash")
    private String passwordHash;
    
    private String role;
    
    @Column(name = "date_created")
    private LocalDateTime dateCreated = LocalDateTime.now();
    
    public user() {}
    
    public Integer getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public LocalDateTime getDateCreated() { return dateCreated; }

    public void setUserId(Integer userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setRole(String role) { this.role = role; }
    public void setDateCreated(LocalDateTime dateCreated) { this.dateCreated = dateCreated; }
}