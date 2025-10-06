package com.devanshvyas.BackblazeStorage.controller;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.UserDto;
import com.devanshvyas.BackblazeStorage.model.StorageConfig;
import com.devanshvyas.BackblazeStorage.model.User;
import com.devanshvyas.BackblazeStorage.model.UserPrincipal;
import com.devanshvyas.BackblazeStorage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("register")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody User user) {
        return service.login(user);
    }

    @PostMapping("storageConfig")
    public ResponseEntity<ApiResponse<UserDto>> configStorage(@RequestBody StorageConfig config, @AuthenticationPrincipal UserPrincipal principal) {
        return service.configStorage(principal.getUsername(), config);
    }
}
