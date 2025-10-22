package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.StudentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/students")
public interface StudentRestController {
    @GetMapping
    ResponseEntity<List<StudentDTO>> getAllStudents();

    @GetMapping("/{studentId}")
    ResponseEntity<StudentDTO> getStudentByID(@PathVariable Long studentId);
}
