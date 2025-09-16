package com.school_management.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEnrollDTO {
    @NotNull
    private Long courseId;
    @NotNull
    private Long studentId;
}
