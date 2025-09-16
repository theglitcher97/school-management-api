package com.school_management.api.services.impls;

import com.school_management.api.dto.PostEnrollDTO;
import com.school_management.api.entities.Course;
import com.school_management.api.entities.Enrollment;
import com.school_management.api.entities.Student;
import com.school_management.api.repositories.CourseRepository;
import com.school_management.api.repositories.EnrollmentRepository;
import com.school_management.api.repositories.StudentRepository;
import com.school_management.api.services.interfaces.EnrollmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public void enroll(PostEnrollDTO postEnrollDTO) {
        logger.info("Fetching course with ID {}", postEnrollDTO.getCourseId());
        Course course = this.courseRepository.findById(postEnrollDTO.getCourseId())
                .orElseThrow(() -> {
                    logger.error("Couldn't find course with id {}", postEnrollDTO.getCourseId());
                    return new EntityNotFoundException("Course not found");
                });

        logger.info("Fetching student with ID {}", postEnrollDTO.getStudentId());
        Student student = this.studentRepository.findById(postEnrollDTO.getStudentId())
                .orElseThrow(() -> {
                    logger.error("Couldn't find student with id {}", postEnrollDTO.getStudentId());
                    return new EntityNotFoundException("Student not found");
                });

        logger.info("Saving enrollment student [{}] --> course [{}]", postEnrollDTO.getStudentId(), postEnrollDTO.getCourseId());
        this.enrollmentRepository.save(Enrollment.builder().course(course).student(student).build());
    }

    @Override
    @Transactional
    public void remove(Long enrollId) {
        logger.info("Fetching enrollment with ID {}", enrollId);
        this.enrollmentRepository.findById(enrollId)
                .ifPresentOrElse(this.enrollmentRepository::delete,
                        () -> {
                    logger.error("Enrollment with ID {} not found", enrollId);
                    throw new EntityNotFoundException("Enrollment not found");
                });

    }
}
