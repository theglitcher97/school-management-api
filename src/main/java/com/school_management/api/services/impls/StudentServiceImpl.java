package com.school_management.api.services.impls;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.entities.Student;
import com.school_management.api.entities.User;
import com.school_management.api.repositories.StudentRepository;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final String INIT_CODE = "S0000001";
    private final StudentRepository studentRepository;

    @Override
    @Transactional
    public StudentDTO createStudent(String firstName, String lastName, User user) {
        String latestStudentCode = this.studentRepository.getLatestStudentCode();
        Student student =
                Student.builder()
                        .firstName(firstName)
                        .lastName(firstName)
                        .code(Objects.isNull(latestStudentCode) ? INIT_CODE : AppUtils.getNextCode(latestStudentCode))
                        .user(user)
                        .build();

        student = this.studentRepository.save(student);
        return new StudentDTO(student.getFirstName(), student.getLastName(), student.getCode());
    }
}
