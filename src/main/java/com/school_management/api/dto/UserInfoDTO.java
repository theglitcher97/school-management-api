package com.school_management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String email;
    private String role;
    private String firstName;
    private String lastName;
    private String code;
}
