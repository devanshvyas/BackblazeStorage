package com.devanshvyas.BackblazeStorage.dto;

import com.devanshvyas.BackblazeStorage.model.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String email;
    private UserRole role;
    private Boolean isStorageConfigured;
    private String token;
}
