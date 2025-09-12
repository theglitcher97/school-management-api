package com.school_management.api.services.interfaces;

import com.school_management.api.dto.LoginDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);
}
