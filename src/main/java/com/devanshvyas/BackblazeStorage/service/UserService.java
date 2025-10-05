package com.devanshvyas.BackblazeStorage.service;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.model.User;
import com.devanshvyas.BackblazeStorage.repo.UserRepo;
import com.devanshvyas.BackblazeStorage.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    public ResponseEntity<ApiResponse<String>> register(User user) throws DataIntegrityViolationException {
        try {
            repo.save(user);
        } catch (DataIntegrityViolationException e) {
            throw e;
        }
        return ResponseUtil.success("Registered user successfully!", "Registered user successfully!");
    }

    public ResponseEntity<ApiResponse<String>> login(User user) {
        User dbUser = repo.findByUsername(user.getUsername());
        if (dbUser == null) {
            return ResponseUtil.error("User not found", "User not found");
        } else {
            if (Objects.equals(dbUser.getPassword(), user.getPassword())) {
                return ResponseUtil.success("Login Successful", "Login Successful");
            } else {
                return ResponseUtil.error("Incorrect credentials", "Incorrect credentials");
            }
        }
    }
}
