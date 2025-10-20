package com.school_management.api.dto;

import com.school_management.api.enums.USER_TYPE;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserAccountDTO {
    private String email;
    private String firstName;
    private String lastName;
    private USER_TYPE userType;
}
