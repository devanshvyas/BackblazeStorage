package com.devanshvyas.BackblazeStorage.dto;

import com.devanshvyas.BackblazeStorage.model.user.User;
import com.devanshvyas.BackblazeStorage.model.user.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private UserRole role;
    private Boolean isStorageConfigured;
    private String token;

    public UserDto(User user, String jwtToken) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.isStorageConfigured = this.role == UserRole.ADMIN ? (user.getStorageConfig() != null) : null;
        this.token = jwtToken;
    }
}
