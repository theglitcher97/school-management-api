package com.school_management.api.services.impls;

import com.school_management.api.dto.CourseDTO;
import com.school_management.api.dto.CourseStudentsDTO;
import com.school_management.api.dto.CreateCourseDTO;
import com.school_management.api.dto.StudentDTO;
import com.school_management.api.entities.Course;
import com.school_management.api.entities.Teacher;
import com.school_management.api.repositories.CourseRepository;
import com.school_management.api.repositories.TeacherRepository;
import com.school_management.api.services.interfaces.CourseService;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.services.interfaces.TeacherService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;


    @Override
    @Transactional
    public CourseDTO createCourse(CreateCourseDTO createCourseDTO) {
        logger.info("Starting process to create a new course");

        logger.info("Searching for teacher with id {}",createCourseDTO.getTeacherId());
        Teacher teacher = this.teacherRepository.findById(createCourseDTO.getTeacherId()).orElseThrow(() -> {
            logger.error("Teacher with id {} was not found, course couldn't be created", createCourseDTO.getTeacherId());
            return new EntityNotFoundException("Teacher with id "+createCourseDTO.getTeacherId()+" not found");
        });

        logger.info("Creating course instance link to teacher with id {}", teacher.getId());
        Course course = Course.builder()
                .name(createCourseDTO.getName())
                .description(createCourseDTO.getDescription())
                .teacher(teacher)
                .build();

        logger.info("Saving new course");
        this.courseRepository.save(course);

        return new CourseDTO(course.getId(), course.getName(), course.getDescription(), this.teacherService.getBydId(teacher.getId()));
    }

    @Override
    public CourseDTO getCourseById(Long courseId) {
        logger.info("Searching for Course with id {}", courseId);
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Course with id {} not found", courseId);
            return new EntityNotFoundException("Course not found");
        });

        return new CourseDTO(course.getId(), course.getName(), course.getDescription(), this.teacherService.getBydId(course.getTeacher().getId()));
    }

    @Override
    public List<CourseDTO> getCourses() {
        return this.courseRepository.findAll().stream()
                .map(this::mapEntityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public void removeCourse(Long courseId) {
        this.courseRepository.findById(courseId)
                .ifPresent(this.courseRepository::delete);
    }

    @Override
    public CourseStudentsDTO getCourseWithStudents(Long courseId) {
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Course with id {} not found", courseId);
            return new EntityNotFoundException("Course not found");
        });

        Set<StudentDTO> studentDTOS = course.getEnrollments().stream()
                .map(enrollment -> this.studentService.entityToDTO(enrollment.getStudent()))
                .collect(Collectors.toSet());

        CourseDTO courseDTO = this.mapEntityToDTO(course);

        return new CourseStudentsDTO(courseDTO, studentDTOS);
    }

    private CourseDTO mapEntityToDTO(Course course) {
        return new CourseDTO(course.getId(), course.getName(), course.getDescription(), this.teacherService.getBydId(course.getTeacher().getId()));
    }
}
