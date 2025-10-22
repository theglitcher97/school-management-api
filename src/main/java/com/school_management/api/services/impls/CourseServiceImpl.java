package com.school_management.api.services.impls;

import com.school_management.api.dto.CourseDTO;
import com.school_management.api.dto.CourseStudentsDTO;
import com.school_management.api.dto.CreateCourseDTO;
import com.school_management.api.dto.StudentDTO;
import com.school_management.api.entities.Course;
import com.school_management.api.entities.Teacher;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.policies.CourseAccessPolicy;
import com.school_management.api.policies.UserAccessPolicy;
import com.school_management.api.repositories.CourseRepository;
import com.school_management.api.repositories.TeacherRepository;
import com.school_management.api.services.interfaces.CourseService;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.services.interfaces.TeacherService;
import com.school_management.api.utils.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
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
    private final CurrentUserProvider currentUserProvider;
    private final UserAccessPolicy userAccessPolicy;


    @Override
    @Transactional
    public CourseDTO createCourse(CreateCourseDTO createCourseDTO) {
        logger.info("Starting process to create a new course");
        User user = this.currentUserProvider.getCurrentUser();
        UserAccessPolicy.assertCanCreateCourse(user);

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
        User user = this.currentUserProvider.getCurrentUser();
        logger.info("Searching for Course with id {}", courseId);
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Course with id {} not found", courseId);
            return new EntityNotFoundException("Course not found");
        });

        this.userAccessPolicy.assertCanReadCourse(user, course);

        return new CourseDTO(course.getId(), course.getName(), course.getDescription(), this.teacherService.getBydId(course.getTeacher().getId()));
    }

    @Override
    public List<CourseDTO> getCoursesForUser(User user) {
        List<Course> courses;
        switch (USER_ROLE.valueOf(user.getRole())) {
            case ROLE_STUDENT -> courses = this.courseRepository.findByStudentId(user.getId());
            case ROLE_TEACHER -> courses = this.courseRepository.findByTeacherId(user.getId());
            default -> courses = this.courseRepository.findAll();
        }

        return courses.stream()
                .map(this::mapEntityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public void removeCourse(Long courseId) {
        this.userAccessPolicy.assertCanDeleteCourse(this.currentUserProvider.getCurrentUser());
        this.courseRepository.findById(courseId)
                .ifPresent(this.courseRepository::delete);
    }

    @Override
    public Set<StudentDTO> getCourseWithStudents(Long courseId) {
        User user = this.currentUserProvider.getCurrentUser();
        Course course = this.courseRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Course with id {} not found", courseId);
            return new EntityNotFoundException("Course not found");
        });

        this.userAccessPolicy.assertCanReadCourseStudents(user, course);

        return course.getEnrollments().stream()
                .map(enrollment -> this.studentService.entityToDTO(enrollment.getStudent()))
                .collect(Collectors.toSet());
    }

    private CourseDTO mapEntityToDTO(Course course) {
        return new CourseDTO(course.getId(), course.getName(), course.getDescription(), this.teacherService.getBydId(course.getTeacher().getId()));
    }
}
