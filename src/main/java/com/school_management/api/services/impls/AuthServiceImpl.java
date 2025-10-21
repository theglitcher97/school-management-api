package com.school_management.api.services.impls;

import com.school_management.api.dto.LoginCredentialsDTO;
import com.school_management.api.dto.LoginDTO;
import com.school_management.api.entities.User;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.interfaces.AuthService;
import com.school_management.api.services.interfaces.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final Long MINUTE = 1000 * 60L;
    private static final Long TOKEN_EXP_TIME = MINUTE * 30;
    private static final Long REFRESH_TOKEN_EXP_TIME = MINUTE * 60 * 24;

    @Override
    public LoginCredentialsDTO login(LoginDTO loginDTO) throws AuthenticationException {
        // validate that the email and password are correct, if not throws an exception
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        User user = this.userRepository.findByUsername(loginDTO.getEmail()).get();
        String token = this.jwtService.generateToken(Map.of(), user, TOKEN_EXP_TIME);
        String refreshToken = this.jwtService.generateToken(Map.of(), user, REFRESH_TOKEN_EXP_TIME);
        return new LoginCredentialsDTO(token, refreshToken, System.currentTimeMillis() + TOKEN_EXP_TIME);
    }

    @Override
    public LoginCredentialsDTO refreshToken() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = this.jwtService.generateToken(Map.of(), user, TOKEN_EXP_TIME);
        return new LoginCredentialsDTO(token, "", System.currentTimeMillis() + TOKEN_EXP_TIME);
    }
}
