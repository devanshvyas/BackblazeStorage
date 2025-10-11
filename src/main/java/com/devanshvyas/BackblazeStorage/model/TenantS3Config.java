package com.devanshvyas.BackblazeStorage.model;

import lombok.Data;
import software.amazon.awssdk.services.s3.S3Client;

@Data
public class TenantS3Config {
    private final S3Client client;
    private final StorageConfig config;

    public TenantS3Config(S3Client client, StorageConfig config) {
        this.client = client;
        this.config = config;
    }
}
