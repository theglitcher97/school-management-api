package com.school_management.api.exceptions;

import com.school_management.api.dto.CommonErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EntityNotFoundExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonErrorResponse> errorHandler(EntityNotFoundException e) {
        return new ResponseEntity<>(new CommonErrorResponse(e.getMessage(), "ENTITY_NOT_FOUND", 404), HttpStatus.BAD_REQUEST);
    }
}
