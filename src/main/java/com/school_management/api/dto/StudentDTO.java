package com.school_management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StudentDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String code;
}

