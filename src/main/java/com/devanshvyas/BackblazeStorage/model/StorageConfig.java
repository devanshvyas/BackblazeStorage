package com.devanshvyas.BackblazeStorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "storage_configs")
public class StorageConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private Boolean configured = false;

    // Backblaze B2 specific fields
    @Column(name = "b2_application_key_id")
    private String b2ApplicationKeyId;

    @Column(name = "b2_application_key", columnDefinition = "TEXT")
    private String b2ApplicationKey; // Encrypted

    @Column(name = "b2_bucket_name")
    private String b2BucketName;

    @Column(name = "b2_bucket_id")
    private String b2BucketId;
}
