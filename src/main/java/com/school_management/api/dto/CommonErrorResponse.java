package com.school_management.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class CommonErrorResponse {
    private String message;
    private String exception;
    private int status;
}
