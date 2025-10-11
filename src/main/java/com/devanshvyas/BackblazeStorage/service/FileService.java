package com.devanshvyas.BackblazeStorage.service;

import com.devanshvyas.BackblazeStorage.config.multitenancy.AppTenantContext;
import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.model.ContentType;
import com.devanshvyas.BackblazeStorage.model.GalleryMetadata;
import com.devanshvyas.BackblazeStorage.model.User;
import com.devanshvyas.BackblazeStorage.repo.GalleryMetadataRepo;
import com.devanshvyas.BackblazeStorage.repo.UserRepo;
import com.devanshvyas.BackblazeStorage.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GalleryMetadataRepo galleryMetadataRepo;

    public ResponseEntity<ApiResponse<String>> uploadFile(String email, MultipartFile file) {
        String currentTenant = AppTenantContext.getCurrentTenant();
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String b2FileKey = currentTenant + "/" + UUID.randomUUID().toString() + extension;
        try {
            awsS3Service.uploadFile(file);

            GalleryMetadata metadata = new GalleryMetadata();
            metadata.setB2fileKey(b2FileKey);
            metadata.setContentType(ContentType.fromString(extension));
            metadata.setUploadedAt(new Date().toInstant());
            metadata.setName(file.getOriginalFilename());
            metadata.setSize(file.getSize());

            User user = userRepo.findByEmail(email);
            metadata.setOwner(user);

            galleryMetadataRepo.save(metadata);

            return ResponseUtil.success("Uploaded " + file.getOriginalFilename() + " successfully!", "success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}