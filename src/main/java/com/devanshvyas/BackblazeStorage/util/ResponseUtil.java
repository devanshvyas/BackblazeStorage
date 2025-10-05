package com.devanshvyas.BackblazeStorage.util;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("success", message, data);
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatusCode status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("error", message, data);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>("error", message, data);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}