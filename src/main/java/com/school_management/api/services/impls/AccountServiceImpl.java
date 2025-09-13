package com.school_management.api.services.impls;

import com.school_management.api.dto.AccountCreatedDTO;
import com.school_management.api.dto.CreateUserAccountDTO;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.enums.USER_TYPE;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.interfaces.AccountService;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.services.interfaces.TeacherService;
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
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final TeacherService teacherService;
    private final StudentService studentService;

    @Override
    @Transactional
    public ResponseEntity<AccountCreatedDTO> createAccount(CreateUserAccountDTO accountDTO) {

        this.userRepository.findByUsername(accountDTO.getEmail())
                .ifPresent((user) -> { throw new EntityExistsException("This email already exists");
                });

        // creates and save new account
        String generatedPassword = this.passwordGenerator.generatePassword(DEFAULT_PASSWORD_LENGTH);
        User newAccount =
            User.builder()
                .username(accountDTO.getEmail())
                .role(
                    accountDTO.getUserType() == USER_TYPE.STUDENT
                        ? USER_ROLE.STUDENT.getValue()
                        : USER_ROLE.TEACHER.getValue())
                .password(this.passwordEncoder.encode(generatedPassword))
                .build();

        this.userRepository.save(newAccount);

        // creates and save the related entity teacher/student
        String code = "";

        switch (accountDTO.getUserType()){
            case TEACHER -> code = this.teacherService.createTeacher(accountDTO.getFirstName(), accountDTO.getLastName(), newAccount).getCode();
            case STUDENT -> code = this.studentService.createStudent(accountDTO.getFirstName(), accountDTO.getLastName(), newAccount).getCode();
        }

        // todo: send credential to user by email
        AccountCreatedDTO accountCreatedDTO = new AccountCreatedDTO(accountDTO.getEmail(), generatedPassword, code);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountCreatedDTO);
    }
}
