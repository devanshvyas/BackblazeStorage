package com.devanshvyas.BackblazeStorage.repo;

import com.devanshvyas.BackblazeStorage.model.GalleryMetadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryMetadataRepo extends JpaRepository<GalleryMetadata, Integer> {
    @Modifying
    @Transactional
    @Query("delete from GalleryMetadata g where g.b2fileKey = :key")
    void deleteByB2FileKey(@Param("key") String key);

    @Modifying
    @Transactional
    @Query("delete from GalleryMetadata g where g.b2fileKey in :keys")
    void deleteByB2FileKeyIn(@Param("keys") List<String> keys);
}
