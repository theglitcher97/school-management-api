package com.school_management.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class CourseStudentsDTO {
    private CourseDTO courseDTO;
    private Set<StudentDTO> studentDTOS;
}
