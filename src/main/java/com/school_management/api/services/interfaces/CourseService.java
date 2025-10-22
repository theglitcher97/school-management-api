package com.school_management.api.services.interfaces;

import com.school_management.api.dto.CourseDTO;
import com.school_management.api.dto.CourseStudentsDTO;
import com.school_management.api.dto.CreateCourseDTO;
import com.school_management.api.entities.User;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CreateCourseDTO createCourseDTO);

    CourseDTO getCourseById(Long courseId);

    List<CourseDTO> getCoursesForUser(User user);

    void removeCourse(Long courseId);

    CourseStudentsDTO getCourseWithStudents(Long courseId);
}
