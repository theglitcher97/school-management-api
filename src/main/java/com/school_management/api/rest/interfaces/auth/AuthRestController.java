package com.school_management.api.rest.interfaces.auth;

import com.school_management.api.dto.LoginDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
public interface AuthRestController {

    @PostMapping("/login")
    String login(@Validated @RequestBody LoginDTO loginDTO);
}
