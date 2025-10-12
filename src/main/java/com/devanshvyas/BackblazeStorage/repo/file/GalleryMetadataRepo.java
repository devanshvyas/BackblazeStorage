package com.devanshvyas.BackblazeStorage.repo.file;

import com.devanshvyas.BackblazeStorage.model.metadata.GalleryMetadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GalleryMetadataRepo extends BaseMetadataRepo<GalleryMetadata, Integer> {

}
