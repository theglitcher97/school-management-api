package com.school_management.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PatchCourseDTO {
    private String name;
    private String description;
    private Long teacherId;
    private Boolean status;
}
