package com.school_management.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String code;
}

