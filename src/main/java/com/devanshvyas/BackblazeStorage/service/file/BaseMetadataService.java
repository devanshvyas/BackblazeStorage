package com.devanshvyas.BackblazeStorage.service.file;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.FileDTO;
import com.devanshvyas.BackblazeStorage.model.metadata.BaseMetadata;
import com.devanshvyas.BackblazeStorage.model.metadata.ContentType;
import com.devanshvyas.BackblazeStorage.model.user.User;
import com.devanshvyas.BackblazeStorage.repo.file.BaseMetadataRepo;
import com.devanshvyas.BackblazeStorage.repo.user.UserRepo;
import com.devanshvyas.BackblazeStorage.service.aws.AwsS3Service;
import com.devanshvyas.BackblazeStorage.util.ResponseUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public abstract class BaseMetadataService<T extends BaseMetadata> {

    private final BaseMetadataRepo<T, Integer> repo;
    private final String path;

    protected abstract T createNewInstance();

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private UserRepo userRepo;

    public BaseMetadataService(BaseMetadataRepo<T, Integer> repo, String path) {
        this.repo = repo;
        this.path = path;
    }

    public ResponseEntity<ApiResponse<String>> uploadFile(String email, MultipartFile file) {
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String randomFileName = UUID.randomUUID().toString() + extension;
        String b2FileKey = path + "/" + randomFileName;

        try {
            awsS3Service.uploadFile(file, b2FileKey);

            T metadata = createNewInstance();
            metadata.setB2fileKey(randomFileName);
            metadata.setContentType(ContentType.fromString(extension));
            metadata.setUploadedAt(new Date().toInstant());
            metadata.setName(file.getOriginalFilename());
            metadata.setSize(file.getSize());

            User user = userRepo.findByEmail(email);
            metadata.setOwner(user);

            repo.save(metadata);

            return ResponseUtil.success("Uploaded " + file.getOriginalFilename() + " successfully!", "success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Resource> downloadFile(String fileName) {
        try {
            T metadata = repo.findByB2fileKey(fileName);
            metadata.setDownloadCount(metadata.getDownloadCount() + 1);
            repo.save(metadata);

            return ResponseEntity.ok().body(awsS3Service.downloadFile(path + "/" + fileName));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteFile(String fileName) {
        try {
            String filenamesWithPath = path + "/" + fileName;
            awsS3Service.deleteFile(filenamesWithPath);
            repo.deleteByB2fileKey(fileName);
            return ResponseUtil.success("Deleted " + fileName + " successfully", "Deleted " + fileName + " successfully");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public ResponseEntity<ApiResponse<String>> deleteFiles(List<String> filenames) {
        if (!filenames.isEmpty()) {
            String path = this.path + "/";
            List<String> filenamesWithPath = filenames.stream().map(name -> path + name).toList();

            awsS3Service.deleteFiles(filenamesWithPath);

            repo.deleteByB2fileKeyIn(filenames);

            return ResponseUtil.success("Deleted " + filenames.toString() + " successfully", "Deleted " + filenames.toString() + " successfully");
        } else {
            return ResponseUtil.error("Error", "File name should not be empty");
        }
    }

    public ResponseEntity<ApiResponse<FileDTO>> fetchAllFiles(int page, int size) {
        try {
            if (size != 0) {
                Sort sort = Sort.by(Sort.Direction.ASC, "uploadedAt");
                Pageable pageable = PageRequest.of(page, size, sort);
                Page<BaseMetadata> pageData = (Page<BaseMetadata>) repo.findAll(pageable);
                FileDTO fileDTO = new FileDTO(pageData);
                return ResponseUtil.success("Fetched all data successfully", fileDTO);
            } else {
                FileDTO fileDTO = new FileDTO((List<BaseMetadata>) repo.findAll());
                return ResponseUtil.success("Fetched all data successfully", fileDTO);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
