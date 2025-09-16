package com.school_management.api.rest.impls;

import com.school_management.api.dto.PostEnrollDTO;
import com.school_management.api.rest.interfaces.EnrollmentRestController;
import com.school_management.api.services.interfaces.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EnrollmentRestControllerImpl implements EnrollmentRestController {
    private final EnrollmentService enrollmentService;

    @Override
    public ResponseEntity enrollStudentInCourse(PostEnrollDTO postEnrollDTO) {
        this.enrollmentService.enroll(postEnrollDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity removeEnrollment(Long enrollId) {
        this.enrollmentService.remove(enrollId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
