package com.devanshvyas.BackblazeStorage.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "gallery_metadata")
public class GalleryMetadata {
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

    @Column(name = "uploaded_at")
    private Instant uploadedAt;
}
