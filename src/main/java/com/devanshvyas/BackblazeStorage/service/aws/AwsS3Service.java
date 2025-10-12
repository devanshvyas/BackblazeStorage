package com.devanshvyas.BackblazeStorage.service.aws;

import com.devanshvyas.BackblazeStorage.model.tenant.TenantS3Config;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.List;

@Service
public class AwsS3Service {

    @Autowired
    private TenantS3Service tenantS3Service;

    public String uploadFile(MultipartFile file, String fileKey) {
        try {
            TenantS3Config config = tenantS3Service.getTenantS3Config();
            String fileName = file.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(config.getConfig().getBucketName())
                    .key(fileKey)
                    .build();
            PutObjectResponse response = config.getClient()
                    .putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            System.out.println("Upload Successfully " + response.eTag());
            return response.eTag();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Resource downloadFile(String filename) {
        try {
            TenantS3Config config = tenantS3Service.getTenantS3Config();
            S3Client client = config.getClient();
            String bucketName = config.getConfig().getBucketName();

            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            ResponseInputStream<GetObjectResponse> responseInputStream = client.getObject(request);
            return new InputStreamResource(responseInputStream);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteFile(String filename) {
        try {
            TenantS3Config config = tenantS3Service.getTenantS3Config();
            S3Client client = config.getClient();
            String bucketName = config.getConfig().getBucketName();

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            client.deleteObject(request);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteFiles(List<String> fileNames) {
        try {
            TenantS3Config config = tenantS3Service.getTenantS3Config();
            S3Client client = config.getClient();
            String bucketName = config.getConfig().getBucketName();

            List<ObjectIdentifier> objectIdentifiers = fileNames.stream()
                    .map(name -> ObjectIdentifier.builder()
                            .key(name)
                            .build())
                    .toList();

            Delete delete = Delete.builder()
                    .objects(objectIdentifiers)
                    .build();

            DeleteObjectsRequest request = DeleteObjectsRequest.builder()
                    .bucket(bucketName)
                    .delete(delete)
                    .build();

            client.deleteObjects(request);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
