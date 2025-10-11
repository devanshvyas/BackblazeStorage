package com.devanshvyas.BackblazeStorage.repo;

import com.devanshvyas.BackblazeStorage.model.GalleryMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryMetadataRepo extends JpaRepository<GalleryMetadata, Integer> {
}
