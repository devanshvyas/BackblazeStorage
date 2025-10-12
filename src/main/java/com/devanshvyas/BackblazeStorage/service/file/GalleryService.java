package com.devanshvyas.BackblazeStorage.service.file;

import com.devanshvyas.BackblazeStorage.model.metadata.GalleryMetadata;
import com.devanshvyas.BackblazeStorage.repo.file.GalleryMetadataRepo;
import com.devanshvyas.BackblazeStorage.util.Constants;
import org.springframework.stereotype.Service;

@Service
public class GalleryService extends BaseMetadataService<GalleryMetadata> {
    public GalleryService(GalleryMetadataRepo repo) {
        super(repo, Constants.GALLERY);
    }

    @Override
    protected GalleryMetadata createNewInstance() {
        return new GalleryMetadata();
    }
}
