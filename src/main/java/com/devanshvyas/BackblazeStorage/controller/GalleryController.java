package com.devanshvyas.BackblazeStorage.controller;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.FileDTO;
import com.devanshvyas.BackblazeStorage.model.config.UserPrincipal;
import com.devanshvyas.BackblazeStorage.service.file.GalleryService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService service;

    public GalleryController(GalleryService service) {
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
    public ResponseEntity<ApiResponse<FileDTO>> fetchAllFiles() {
        return service.fetchAllFiles();
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
