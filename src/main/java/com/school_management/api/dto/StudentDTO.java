package com.school_management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentDTO {
    private String firstName;
    private String lastName;
    private String code;
}

