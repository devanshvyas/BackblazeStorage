package com.devanshvyas.BackblazeStorage.controller;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.UserDto;
import com.devanshvyas.BackblazeStorage.model.FileWrapper;
import com.devanshvyas.BackblazeStorage.model.UserPrincipal;
import com.devanshvyas.BackblazeStorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private FileService service;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> uploadFile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("file") MultipartFile file) {
        return service.uploadFile(userPrincipal.getEmail(), file);
    }

    @GetMapping("{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fileName) {
        return service.downloadFile(fileName);
    }

    @DeleteMapping("{filename:.+}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable("filename") String fileName) {
        return service.deleteFile(fileName);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteMultipleFile(@RequestBody FileWrapper fileWrapper) {
        return service.deleteFiles(fileWrapper.getFilenames());
    }
}
