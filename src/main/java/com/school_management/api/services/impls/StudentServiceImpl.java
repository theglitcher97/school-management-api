package com.school_management.api.services.impls;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.entities.Student;
import com.school_management.api.entities.User;
import com.school_management.api.repositories.StudentRepository;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.utils.AppUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final String INIT_CODE = "S0000001";
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    @Transactional
    public StudentDTO createStudent(String firstName, String lastName, User user) {
        logger.info("Getting latest student code");
        String latestStudentCode = this.studentRepository.getLatestStudentCode();

        logger.info("Creating student instance");
        Student student =
                Student.builder()
                        .firstName(firstName)
                        .lastName(firstName)
                        .code(Objects.isNull(latestStudentCode) ? INIT_CODE : AppUtils.getNextCode(latestStudentCode))
                        .user(user)
                        .build();

        logger.info("Saving student with code {}", student.getCode());
        student = this.studentRepository.save(student);

        logger.info("Returning student dto after saving student");
        return new StudentDTO(student.getId(), user.getUsername(), student.getFirstName(), student.getLastName(), student.getCode());
    }

    @Override
    public List<StudentDTO> getAll() {
        return this.studentRepository.findAllWithEmail();
    }

    @Override
    public StudentDTO getCurrentStudentInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.studentRepository.findByIdWithEmail(user.getId()).get();
    }

    @Override
    public StudentDTO getBydId(Long studentId) {
        return this.studentRepository.findByIdWithEmail(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with id "+studentId+" not found"));
    }
}
