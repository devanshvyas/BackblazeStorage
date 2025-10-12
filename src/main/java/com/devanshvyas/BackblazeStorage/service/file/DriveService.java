package com.devanshvyas.BackblazeStorage.service.file;

import com.devanshvyas.BackblazeStorage.model.metadata.DriveMetadata;
import com.devanshvyas.BackblazeStorage.repo.file.DriveMetadataRepo;
import com.devanshvyas.BackblazeStorage.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class DriveService extends BaseMetadataService<DriveMetadata> {
    public DriveService(DriveMetadataRepo repo) {
        super(repo, Constants.DRIVE);
    }

    @Override
    protected DriveMetadata createNewInstance() {
        return new DriveMetadata();
    }
}
