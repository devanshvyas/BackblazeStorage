package com.devanshvyas.BackblazeStorage.exception;

import com.devanshvyas.BackblazeStorage.dto.ApiResponse;
import com.devanshvyas.BackblazeStorage.util.ResponseUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleDataIntegrityException(DataIntegrityViolationException exception) {
        String message = "A database integrity constraint are violated";

        if (exception.getMostSpecificCause().getMessage().contains("violates unique constraint")) {
            message = "This username is already taken. Please choose a different one.";
        }
        return ResponseUtil.error(HttpStatus.CONFLICT, message, message);
    }
}
