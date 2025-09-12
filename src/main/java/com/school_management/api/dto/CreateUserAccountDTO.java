package com.school_management.api.dto;

import com.school_management.api.enums.USER_TYPE;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserAccountDTO {
    private String email;
    private String firstName;
    private String lastName;
    private USER_TYPE userType;
}
