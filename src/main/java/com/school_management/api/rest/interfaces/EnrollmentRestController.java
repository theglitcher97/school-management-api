package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.PostEnrollDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/enrollment")
public interface EnrollmentRestController {
    @PostMapping
    ResponseEntity enrollStudentInCourse(@RequestBody PostEnrollDTO postEnrollDTO);

    @DeleteMapping("/{enrollId}")
    ResponseEntity removeEnrollment(@PathVariable Long enrollId);

}
