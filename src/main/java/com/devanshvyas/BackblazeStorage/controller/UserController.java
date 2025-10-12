package com.devanshvyas.BackblazeStorage.controller;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.UserDto;
import com.devanshvyas.BackblazeStorage.model.user.StorageConfig;
import com.devanshvyas.BackblazeStorage.model.user.User;
import com.devanshvyas.BackblazeStorage.model.config.UserPrincipal;
import com.devanshvyas.BackblazeStorage.service.user.UserService;
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
        return service.register(user, null);
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<UserDto>> login(@RequestBody User user) {
        return service.login(user);
    }

    @PostMapping("config-storage")
    public ResponseEntity<ApiResponse<UserDto>> configStorage(@RequestBody StorageConfig config, @AuthenticationPrincipal UserPrincipal principal) {
        return service.configStorage(principal.getUsername(), config);
    }

    @PostMapping("register-sub-user")
    public ResponseEntity<ApiResponse<UserDto>> registerSubUser(@RequestBody User user, @AuthenticationPrincipal UserPrincipal principal) {
        return service.register(user, principal.getUsername());
    }
}
