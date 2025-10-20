package com.school_management.api.rest.impls;

import com.school_management.api.dto.LoginCredentialsDTO;
import com.school_management.api.dto.LoginDTO;
import com.school_management.api.rest.interfaces.AuthRestController;
import com.school_management.api.services.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthRestControllerImpl implements AuthRestController {
    private final AuthService authService;

    @Override
    public ResponseEntity<LoginCredentialsDTO> login(LoginDTO loginDTO) {
        return ResponseEntity.ok().body(this.authService.login(loginDTO));
    }

    @Override
    public ResponseEntity<LoginCredentialsDTO> refreshToken() {
        return ResponseEntity.ok().body(this.authService.refreshToken());
    }
}
