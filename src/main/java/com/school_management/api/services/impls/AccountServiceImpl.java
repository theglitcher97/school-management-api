package com.school_management.api.services.impls;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.entities.Student;
import com.school_management.api.entities.Teacher;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.enums.USER_TYPE;
import com.school_management.api.repositories.StudentRepository;
import com.school_management.api.repositories.TeacherRepository;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.interfaces.AccountService;
import com.school_management.api.utils.PasswordGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final int DEFAULT_PASSWORD_LENGTH = 10;

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;

    @Override
    @Transactional
    public ResponseEntity<AccountCreatedDTO> createAccount(CreateUserAccountDTO accountDTO) {

        this.userRepository.findByUsername(accountDTO.getEmail())
                .ifPresent((user) -> { throw new EntityExistsException("This email already exists");
                });

        User newAccount =
            User.builder()
                .username(accountDTO.getEmail())
                .role(
                    accountDTO.getUserType() == USER_TYPE.STUDENT
                        ? USER_ROLE.STUDENT.getValue()
                        : USER_ROLE.TEACHER.getValue())
                .password(this.passwordEncoder.encode(this.passwordGenerator.generatePassword(DEFAULT_PASSWORD_LENGTH)))
                .build();

        this.userRepository.save(newAccount);

        Teacher teacher = null;
        Student student = null;

        if (accountDTO.getUserType() == USER_TYPE.TEACHER) {
            // todo: move logic to its own service
            teacher =
                    Teacher.builder()
                            .firstName(accountDTO.getFirstName())
                            .lastName(accountDTO.getLastName())
                            .code("T00001") // todo: generate automatically
                            .user(newAccount)
                            .build();

             this.teacherRepository.save(teacher);


        } else {
            // todo: move logic to its own service
            student =
                    Student.builder()
                            .firstName(accountDTO.getFirstName())
                            .lastName(accountDTO.getLastName())
                            .code("S00001") // todo: generate automatically
                            .user(newAccount)
                            .build();

            this.studentRepository.save(student);
        }

        // todo: send credential to user by email
        AccountCreatedDTO accountCreatedDTO = new AccountCreatedDTO(accountDTO.getEmail(), "generatedPassword", Objects.nonNull(teacher) ? teacher.getCode() : student.getCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreatedDTO);
    }
}
