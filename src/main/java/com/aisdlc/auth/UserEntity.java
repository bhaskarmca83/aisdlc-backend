package com.aisdlc.auth;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String role = "USER";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public UserEntity() {}

    public UserEntity(String username, String email, String password, String role) {
        this.username  = username;
        this.email     = email;
        this.password  = password;
        this.role      = role;
        this.createdAt = Instant.now();
    }

    public Long    getId()        { return id; }
    public String  getUsername()  { return username; }
    public String  getEmail()     { return email; }
    public String  getPassword()  { return password; }
    public String  getRole()      { return role; }
    public Instant getCreatedAt() { return createdAt; }

    public void setUsername(String username)  { this.username = username; }
    public void setEmail(String email)        { this.email = email; }
    public void setPassword(String password)  { this.password = password; }
    public void setRole(String role)          { this.role = role; }
}
