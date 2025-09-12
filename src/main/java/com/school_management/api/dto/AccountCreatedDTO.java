package com.school_management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountCreatedDTO {
    private String email;
    private String password;
    private String code;
}
