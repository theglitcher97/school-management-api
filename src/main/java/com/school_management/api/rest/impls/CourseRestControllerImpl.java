package com.school_management.api.rest.impls;

import com.school_management.api.dto.CourseDTO;
import com.school_management.api.dto.CourseStudentsDTO;
import com.school_management.api.dto.CreateCourseDTO;
import com.school_management.api.entities.User;
import com.school_management.api.rest.interfaces.CourseRestController;
import com.school_management.api.services.interfaces.CourseService;
import com.school_management.api.utils.CurrentUser;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@AllArgsConstructor
public class CourseRestControllerImpl implements CourseRestController {
    private static final Logger logger = LoggerFactory.getLogger(CourseRestControllerImpl.class);
    private final CourseService courseService;

    @Override
    public ResponseEntity<CourseDTO> createCourse(CreateCourseDTO createCourseDTO) {
        logger.info("Calling create course endpoint");
        return new ResponseEntity<>(this.courseService.createCourse(createCourseDTO), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<CourseDTO>> getCourses(@CurrentUser User user) {
        return new ResponseEntity<>(this.courseService.getCoursesForUser(user), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CourseDTO> getCourseById(Long courseId) {
        logger.info("Endpoint: getCourseById + id [{}]", courseId);
        return new ResponseEntity<>(this.courseService.getCourseById(courseId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CourseStudentsDTO> getCourseStudents(Long courseId) {
        return new ResponseEntity<>(this.courseService.getCourseWithStudents(courseId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteCourse(Long courseId) {
        this.courseService.removeCourse(courseId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
