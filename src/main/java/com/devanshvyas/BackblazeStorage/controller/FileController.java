package com.devanshvyas.BackblazeStorage.controller;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.UserDto;
import com.devanshvyas.BackblazeStorage.model.UserPrincipal;
import com.devanshvyas.BackblazeStorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("file")
public class FileController {
    @Autowired
    private FileService service;

    @PostMapping("upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam("file") MultipartFile file) {
        return service.uploadFile(userPrincipal.getEmail(), file);
    }
}
