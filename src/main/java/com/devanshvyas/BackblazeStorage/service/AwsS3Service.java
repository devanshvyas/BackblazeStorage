package com.devanshvyas.BackblazeStorage.service;

import com.devanshvyas.BackblazeStorage.config.multitenancy.AppTenantContext;
import com.devanshvyas.BackblazeStorage.model.StorageConfig;
import com.devanshvyas.BackblazeStorage.model.TenantS3Config;
import com.devanshvyas.BackblazeStorage.model.User;
import com.devanshvyas.BackblazeStorage.repo.StorageConfigRepo;
import com.devanshvyas.BackblazeStorage.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.net.URI;

@Service
public class AwsS3Service {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StorageConfigRepo storageConfigRepo;

    @Autowired
    private TenantS3Service tenantS3Service;

    public String uploadFile(MultipartFile file) {
        try {
            TenantS3Config config = tenantS3Service.getTenantS3Config();
            String fileName = file.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(config.getConfig().getBucketName()).key(fileName)
                    .build();
            PutObjectResponse response = config.getClient()
                    .putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            System.out.println("Upload Successfully " + response.eTag());
            return response.eTag();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
