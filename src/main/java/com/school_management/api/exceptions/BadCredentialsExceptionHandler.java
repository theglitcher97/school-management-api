package com.school_management.api.exceptions;

import com.school_management.api.dto.CommonErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BadCredentialsExceptionHandler {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CommonErrorResponse> badRequestExceptionHandler(BadCredentialsException ex){
        return new ResponseEntity<>(new CommonErrorResponse(
                "please check your email or password and try again",
                "BAD_CREDENTIALS_EXCEPTION",
                400), HttpStatus.BAD_REQUEST);
    }
}
