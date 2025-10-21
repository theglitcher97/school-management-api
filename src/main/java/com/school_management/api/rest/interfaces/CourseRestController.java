package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.CourseDTO;
import com.school_management.api.dto.CourseStudentsDTO;
import com.school_management.api.dto.CreateCourseDTO;
import com.school_management.api.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RequestMapping("/api/v1/courses")
public interface CourseRestController {
    @PostMapping
    ResponseEntity<CourseDTO> createCourse(@Validated @RequestBody CreateCourseDTO createCourseDTO);

    @GetMapping
    ResponseEntity<List<CourseDTO>> getCourses(User user) throws AccessDeniedException;

    @GetMapping("/{courseId}")
    ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId);

    @GetMapping("/{courseId}/students")
    ResponseEntity<CourseStudentsDTO> getCourseStudents(@PathVariable Long courseId);

    @DeleteMapping("/{courseId}")
    ResponseEntity deleteCourse(@PathVariable Long courseId);
}
