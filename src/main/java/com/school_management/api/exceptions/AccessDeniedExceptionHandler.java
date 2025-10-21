package com.school_management.api.exceptions;

import com.school_management.api.dto.CommonErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class AccessDeniedExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonErrorResponse> exceptionHandler(AccessDeniedException ex) {
        return new ResponseEntity<>(new CommonErrorResponse(ex.getMessage(), "NOT_ALLOWED", HttpStatus.FORBIDDEN.value()), HttpStatus.FORBIDDEN);
    }
}
