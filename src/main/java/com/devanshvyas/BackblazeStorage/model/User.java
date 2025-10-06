package com.devanshvyas.BackblazeStorage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    private UserRole role;

    @Column(name = "admin_id")
    private Integer adminId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StorageConfig storageConfig;

    private Instant createdAt;

    private Instant updatedAt;
}
