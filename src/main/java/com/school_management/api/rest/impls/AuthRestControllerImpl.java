package com.school_management.api.rest.impls;

import com.school_management.api.dto.LoginDTO;
import com.school_management.api.rest.interfaces.AuthRestController;
import com.school_management.api.services.interfaces.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthRestControllerImpl implements AuthRestController {
    private final AuthService authService;

    @Override
    public String login(LoginDTO loginDTO) {
        return this.authService.login(loginDTO);
    }
}
