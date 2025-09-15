package com.school_management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String name;
    private String description;
    private TeacherDTO teacher;
}
