package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.*;
import com.school_management.api.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

@RequestMapping("/api/v1/courses")
public interface CourseRestController {
    @PostMapping
    ResponseEntity<CourseDTO> createCourse(@Validated @RequestBody CreateCourseDTO createCourseDTO);

    @GetMapping
    ResponseEntity<List<CourseDTO>> getCourses(User user, SpecificationDTO specificationDTO);

    @GetMapping("/{courseId}")
    ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId);

    @GetMapping("/{courseId}/students")
    ResponseEntity<Set<StudentDTO>> getCourseStudents(@PathVariable Long courseId);

    @PatchMapping("/{courseId}")
    ResponseEntity<CourseDTO> patchCourse(@PathVariable Long courseId, @RequestBody PatchCourseDTO patchCourse);

    @DeleteMapping("/{courseId}")
    ResponseEntity deleteCourse(@PathVariable Long courseId);
}
