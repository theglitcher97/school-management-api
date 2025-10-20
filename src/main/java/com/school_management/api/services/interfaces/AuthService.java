package com.school_management.api.services.interfaces;

import com.school_management.api.dto.LoginCredentialsDTO;
import com.school_management.api.dto.LoginDTO;

public interface AuthService {
    LoginCredentialsDTO login(LoginDTO loginDTO);

    LoginCredentialsDTO refreshToken();
}
