package com.school_management.api.services.impls;

import com.school_management.api.dto.LoginDTO;
import com.school_management.api.entities.User;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.interfaces.AuthService;
import com.school_management.api.services.interfaces.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public String login(LoginDTO loginDTO) {
        // validate that the email and password are correct, if not throws an exception
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        User user = this.userRepository.findByUsername(loginDTO.getEmail()).get();
        return this.jwtService.generateToken(Map.of(), user);
    }
}
