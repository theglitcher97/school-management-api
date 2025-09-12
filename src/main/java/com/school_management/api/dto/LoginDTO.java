package com.school_management.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @Email
    @NotNull
    private String email;

    @Size(min = 8)
    @NotNull
    private String password;
}
