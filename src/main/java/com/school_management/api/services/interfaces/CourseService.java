package com.school_management.api.services.interfaces;

import com.school_management.api.dto.*;
import com.school_management.api.entities.User;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;

public interface CourseService {
    CourseDTO createCourse(CreateCourseDTO createCourseDTO);

    CourseDTO getCourseById(Long courseId);

    List<CourseDTO> getCoursesForUser(User user, SpecificationDTO specificationDTO);

    void removeCourse(Long courseId);

    Set<StudentDTO> getCourseWithStudents(Long courseId);

    CourseDTO patchCourse(Long courseId, PatchCourseDTO patchCourse);
}
