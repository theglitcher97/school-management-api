package com.school_management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginCredentialsDTO {
    private String token;
    private String refreshToken;
    private Long expTime;
}
