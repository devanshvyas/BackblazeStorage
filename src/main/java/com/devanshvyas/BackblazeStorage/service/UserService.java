package com.devanshvyas.BackblazeStorage.service;

import com.devanshvyas.BackblazeStorage.config.multitenancy.AppTenantContext;
import com.devanshvyas.BackblazeStorage.config.multitenancy.TenantService;
import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.dto.UserDto;
import com.devanshvyas.BackblazeStorage.model.StorageConfig;
import com.devanshvyas.BackblazeStorage.model.User;
import com.devanshvyas.BackblazeStorage.model.UserRole;
import com.devanshvyas.BackblazeStorage.repo.StorageConfigRepo;
import com.devanshvyas.BackblazeStorage.repo.UserRepo;
import com.devanshvyas.BackblazeStorage.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private StorageConfigRepo storageRepo;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TenantService tenantService;

    public ResponseEntity<ApiResponse<UserDto>> register(User user, String adminUsername) {
        try {
            String encryptPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptPass);
            String tenantUsername = user.getUsername();
            if (adminUsername != null) {
                user.setAdminUsername(adminUsername);
                user.setRole(UserRole.USER);
                tenantUsername = adminUsername;
            } else {
                user.setRole(UserRole.ADMIN);
                tenantService.createTenant(user.getUsername());
            }
            user.setCreatedAt(Instant.now());
            user.setUpdatedAt(Instant.now());
            userRepo.save(user);
            String jwtToken = jwtService.generateToken(user.getEmail(), tenantUsername);
            UserDto userDto = new UserDto(user.getEmail(),
                    user.getRole(),
                    adminUsername == null ? false : null,
                    jwtToken);
            return ResponseUtil.success("Registered user successfully!", userDto);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
    }

    public ResponseEntity<ApiResponse<UserDto>> login(User user) {
        UsernamePasswordAuthenticationToken userPassToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(userPassToken);
        if (authentication.isAuthenticated()) {
            user = userRepo.findByEmail(user.getEmail());
            String tenantUsername = user.getUsername();
            if (user.getAdminUsername() != null) {
                tenantUsername = user.getAdminUsername();
            }
            String jwtToken = jwtService.generateToken(user.getEmail(), tenantUsername);

            UserDto userDto = new UserDto(user.getEmail(),
                    user.getRole(),
                    user.getStorageConfig() != null ? user.getStorageConfig().getConfigured() : null,
                    jwtToken);
            return ResponseUtil.success("success", userDto);
        }
        return ResponseUtil.error("Login Failed!", null);
    }

    public ResponseEntity<ApiResponse<UserDto>> configStorage(String username, StorageConfig config) {
        try {
            User user = userRepo.findByUsername(username);
            String encryptedKey = encryptionService.encrypt(config.getApplicationKey());
            config.setApplicationKey(encryptedKey);
            config.setConfigured(true);
            config.setUser(user);
            storageRepo.save(config);
            UserDto userDto = new UserDto(user.getEmail(), user.getRole(), true, null);
            return ResponseUtil.success("Storage config updated!", userDto);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
