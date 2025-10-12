package com.devanshvyas.BackblazeStorage.model.user;

import com.devanshvyas.BackblazeStorage.util.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "storage_configs", schema = Constants.DEFAULT_TENANT)
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
    private String applicationKeyId;

    @Column(name = "b2_application_key", columnDefinition = "TEXT")
    private String applicationKey; // Encrypted

    @Column(name = "b2_bucket_name")
    private String bucketName;

    @Column(name = "b2_bucket_id")
    private String bucketId;

    @Column(name = "s3_end_point", nullable = false)
    private String endPoint;

    @Column(name = "s3_region")
    private String region;
}
