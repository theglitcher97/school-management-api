package com.school_management.api.services.interfaces;

import com.school_management.api.dto.CourseDTO;
import com.school_management.api.dto.CourseStudentsDTO;
import com.school_management.api.dto.CreateCourseDTO;

import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CreateCourseDTO createCourseDTO);

    CourseDTO getCourseById(Long courseId);

    List<CourseDTO> getCourses();

    void removeCourse(Long courseId);

    CourseStudentsDTO getCourseWithStudents(Long courseId);
}
