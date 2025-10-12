package com.school_management.api.services;

import com.school_management.api.dto.LoginDTO;
import com.school_management.api.entities.User;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.impls.AuthServiceImpl;
import com.school_management.api.services.interfaces.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldReturnUserToken() {
        // Arrange
        User user = User.builder().id(1L).username("test@gmail.com").password("password").active(true).role("STUDENT").build();
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(), any())).thenReturn("generated_token");

        // Act
        LoginDTO loginDTO = new LoginDTO("test@gmail.com", "password");
        String token = this.authService.login(loginDTO);

        // Assert
        assertEquals("generated_token", token);
    }

    @Test
    void shouldReturnAnExceptionIfEmailOrPasswordIsWrong(){
        // Arrange
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("email or password incorrect"));

        // Act
        LoginDTO loginDTO = new LoginDTO("test@gmail.com", "password");

        // Assert
        AuthenticationException ex = assertThrows(AuthenticationException.class, () -> this.authService.login(loginDTO));
        assertEquals("email or password incorrect", ex.getMessage());
        verify(userRepository, never()).findByUsername(anyString());
        verify(jwtService, never()).generateToken(any(), any());
    }
}
