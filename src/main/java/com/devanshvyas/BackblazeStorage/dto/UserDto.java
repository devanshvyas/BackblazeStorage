package com.devanshvyas.BackblazeStorage.dto;

import com.devanshvyas.BackblazeStorage.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserDto {
    private String email;
    private UserRole role;
    private boolean isStorageConfigured;
    private String token;
}
