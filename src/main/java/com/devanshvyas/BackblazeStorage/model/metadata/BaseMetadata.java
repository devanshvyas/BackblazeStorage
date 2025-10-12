package com.devanshvyas.BackblazeStorage.model.metadata;

import com.devanshvyas.BackblazeStorage.model.user.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public class BaseMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "content_type")
    private ContentType contentType;

    @Column(name = "b2_file_key", unique = true)
    private String b2fileKey;

    private Long size;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;
}
