package com.school_management.api.services.impls;

import com.school_management.api.dto.*;
import com.school_management.api.entities.Course;
import com.school_management.api.entities.Teacher;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        this.userAccessPolicy.assertCanCreateCourse(user);

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

        return this.mapEntityToDTO(course);
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

        return this.mapEntityToDTO(course);
    }

    @Override
    public List<CourseDTO> getCoursesForUser(User user, SpecificationDTO specificationDTO) {
        List<Course> courses;
        Specification<Course> spec = Specification.unrestricted();

        // apply role filter
        switch (USER_ROLE.valueOf(user.getRole())){
            case ROLE_TEACHER -> spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("teacher").get("id"), user.getId()));
            case ROLE_STUDENT ->  spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("enrollments").get("student").get("id"), user.getId()));
        }

        if(Objects.nonNull(specificationDTO.getTerm())) {
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("name")), "%" + specificationDTO.getTerm().toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + specificationDTO.getTerm().toLowerCase() + "%")
            ));
        }

        return this.courseRepository.findAll(spec)
                .stream()
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

    @Override
    @Transactional
    public CourseDTO patchCourse(Long courseId, PatchCourseDTO patchCourse) {
        logger.info("Checking user authority to patch course");
        this.userAccessPolicy.assertCanPatchCourse(this.currentUserProvider.getCurrentUser());

        logger.info("fetching course with id {}", courseId);
        Course course = this.getCourseEntity(courseId);

        logger.info("patching course  information");
        course.setStatus(patchCourse.getStatus());
        if (Objects.nonNull(patchCourse.getName()) && !patchCourse.getName().trim().isBlank())
            course.setName(patchCourse.getName());
        if (Objects.nonNull(patchCourse.getDescription()) && !patchCourse.getDescription().trim().isBlank())
            course.setDescription(patchCourse.getDescription());


        if (Objects.nonNull(patchCourse.getTeacherId()) && patchCourse.getTeacherId() != 0) {
            logger.info("Checking new teacher existence");
            if (!this.teacherRepository.existsById(patchCourse.getTeacherId()))
                throw new EntityNotFoundException("Teacher with ID "+patchCourse.getTeacherId()+" not found");

            if (!Objects.equals(course.getTeacher().getId(), patchCourse.getTeacherId())) {
                logger.info("Updating course assigned teacher");
                course.setTeacher(this.teacherRepository.getReferenceById(patchCourse.getTeacherId()));
            }
        }

        this.courseRepository.save(course);
        return this.mapEntityToDTO(course);
    }

    private CourseDTO mapEntityToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .name(course.getName())
                .status(course.getStatus())
                .description(course.getDescription())
                .teacher(this.mapEntityToDTO(course.getTeacher()))
                .build();
    }

    private TeacherDTO mapEntityToDTO(Teacher teacher) {
        return TeacherDTO.builder()
                .id(teacher.getId())
                .code(teacher.getCode())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .build();
    }

    private Course getCourseEntity(Long courseId){
        return this.courseRepository.findById(courseId).orElseThrow(() -> {
            logger.error("Course with id {} not found", courseId);
            return new EntityNotFoundException("Course not found");
        });
    }
}
