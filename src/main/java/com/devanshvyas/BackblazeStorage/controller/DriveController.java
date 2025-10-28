package com.devanshvyas.BackblazeStorage.controller;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.FileDTO;
import com.devanshvyas.BackblazeStorage.model.config.UserPrincipal;
import com.devanshvyas.BackblazeStorage.service.file.DriveService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/drive")
public class DriveController {

    private final DriveService service;

    public DriveController(DriveService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> uploadFile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("file") MultipartFile file) {
        return service.uploadFile(userPrincipal.getEmail(), file);
    }

    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fileName) {
        return service.downloadFile(fileName);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<FileDTO>> fetchAllFiles(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "0") int size) {
        return service.fetchAllFiles(page, size);
    }

    @DeleteMapping("{filename:.+}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable("filename") String fileName) {
        return service.deleteFile(fileName);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteMultipleFile(@RequestBody FileDTO fileWrapper) {
        return service.deleteFiles(fileWrapper.getFilenames());
    }
}
