package com.devanshvyas.BackblazeStorage.service;

import com.devanshvyas.BackblazeStorage.config.multitenancy.AppTenantContext;
import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.model.ContentType;
import com.devanshvyas.BackblazeStorage.model.FileWrapper;
import com.devanshvyas.BackblazeStorage.model.GalleryMetadata;
import com.devanshvyas.BackblazeStorage.model.User;
import com.devanshvyas.BackblazeStorage.repo.GalleryMetadataRepo;
import com.devanshvyas.BackblazeStorage.repo.UserRepo;
import com.devanshvyas.BackblazeStorage.util.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
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
        String b2FileKey = currentTenant + "-" + UUID.randomUUID().toString() + extension;
        try {
            awsS3Service.uploadFile(file, b2FileKey);

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

    public ResponseEntity<Resource> downloadFile(String fileName) {
        return ResponseEntity.ok().body(awsS3Service.downloadFile(fileName));
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteFile(String fileName) {
        try {
            awsS3Service.deleteFile(fileName);
            galleryMetadataRepo.deleteByB2FileKey(fileName);
            return ResponseUtil.success("Deleted " + fileName + " successfully", "Deleted " + fileName + " successfully");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteFiles(List<String> fileNames) {
        if (!fileNames.isEmpty()) {
            awsS3Service.deleteFiles(fileNames);
            galleryMetadataRepo.deleteByB2FileKeyIn(fileNames);
            return ResponseUtil.success("Deleted " + fileNames.toString() + " successfully", "Deleted " + fileNames.toString() + " successfully");
        } else {
            return ResponseUtil.error("Error", "File name should not be empty");
        }
    }

    public ResponseEntity<ApiResponse<FileWrapper>> fetchAllFiles() {
        try {
            FileWrapper fileWrapper = new FileWrapper();
            fileWrapper.setFiles(galleryMetadataRepo.findAll());
            return ResponseUtil.success("Fetched all data successfully", fileWrapper);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}