package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.LoginCredentialsDTO;
import com.school_management.api.dto.LoginDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthRestController {

    @PostMapping("/login")
    ResponseEntity<LoginCredentialsDTO> login(@Validated @RequestBody LoginDTO loginDTO);

    @PostMapping("/refresh")
    ResponseEntity<LoginCredentialsDTO> refreshToken();
}
