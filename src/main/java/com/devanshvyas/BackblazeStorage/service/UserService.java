package com.devanshvyas.BackblazeStorage.service;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.model.User;
import com.devanshvyas.BackblazeStorage.repo.UserRepo;
import com.devanshvyas.BackblazeStorage.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public ResponseEntity<ApiResponse<String>> register(User user) throws DataIntegrityViolationException {
        try {
            String encryptPass = new BCryptPasswordEncoder(12).encode(user.getPassword());
            user.setPassword(encryptPass);
            repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
        return ResponseUtil.success("Registered user successfully!", "Registered user successfully!");
    }

    public ResponseEntity<ApiResponse<String>> login(User user) {
        UsernamePasswordAuthenticationToken userPassToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(userPassToken);
        if (authentication.isAuthenticated()) {
            String jwtToken = jwtService.generateToken(user.getUsername());
            return ResponseUtil.success("success", jwtToken);
        }
        return ResponseUtil.error("Login Failed!", "Login Failed!");
    }
}
