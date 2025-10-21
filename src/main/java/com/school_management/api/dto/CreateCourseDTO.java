package com.school_management.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateCourseDTO {
    @NotNull
    @Size(max = 255)
    private String name;
    private String description;
    @NotNull
    private Long teacherId;

}
