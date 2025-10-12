package com.devanshvyas.BackblazeStorage.service.aws;

import com.devanshvyas.BackblazeStorage.config.multitenancy.AppTenantContext;
import com.devanshvyas.BackblazeStorage.model.user.StorageConfig;
import com.devanshvyas.BackblazeStorage.model.tenant.TenantS3Config;
import com.devanshvyas.BackblazeStorage.model.user.User;
import com.devanshvyas.BackblazeStorage.repo.user.StorageConfigRepo;
import com.devanshvyas.BackblazeStorage.repo.user.UserRepo;
import com.devanshvyas.BackblazeStorage.service.encryption.EncryptionService;
import com.devanshvyas.BackblazeStorage.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TenantS3Service {
    private final Map<String, TenantS3Config> configCache = new ConcurrentHashMap<>();

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StorageConfigRepo storageConfigRepo;

    @Autowired
    private EncryptionService encryptionService;

    public TenantS3Config getTenantS3Config() {
        String id = AppTenantContext.getCurrentTenant();
        if (id.equals(Constants.DEFAULT_TENANT)) {
            throw new IllegalStateException("TenantS3Service: tenant id not valid!");
        }
        return configCache.computeIfAbsent(id, this::createConfig);
    }

    private TenantS3Config createConfig(String id) {
        try {
            User user = userRepo.findByUsername(id);
            if (user.getAdminUsername() != null) {
                user = userRepo.findByUsername(user.getAdminUsername());
            }

            StorageConfig config = storageConfigRepo.findByUser(user);
            String appKeyId = config.getApplicationKeyId();
            String appKey = encryptionService.decrypt(config.getApplicationKey());
            String endPoint = config.getEndPoint();
            String region = config.getRegion();

            S3Client client = S3Client.builder()
                    .region(Region.of(region))
                    .endpointOverride(URI.create(endPoint))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(appKeyId, appKey)))
                    .build();

            return new TenantS3Config(client, config);
        } catch (Exception e) {
            System.out.println("ERROR: Backblaze config: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }
}
