package com.school_management.api.services.impls;

import com.school_management.api.dto.*;
import com.school_management.api.entities.Teacher;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.enums.USER_TYPE;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.interfaces.UsersService;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.services.interfaces.TeacherService;
import com.school_management.api.utils.PasswordGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {
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

    @Override
    @Transactional
    public String resetPassword(Long userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        String newPassword = this.passwordGenerator.generatePassword(DEFAULT_PASSWORD_LENGTH);

        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(user);
        return newPassword;
    }

    @Override
    public UserInfoDTO getCurrentUserInfo() {
        User user = this.getCurrentUser();
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setEmail(user.getUsername());
        userInfoDTO.setRole(user.getRole().split("_")[1]); // ROLE_X -> X

        if (user.getRole().equals(USER_ROLE.TEACHER.getValue())) {
            TeacherDTO teacher = this.teacherService.getBydId(user.getId());
            userInfoDTO.setFirstName(teacher.getFirstName());
            userInfoDTO.setLastName(teacher.getLastName());
        } else if (user.getRole().equals(USER_ROLE.STUDENT.getValue())){
            StudentDTO student = this.studentService.getBydId(user.getId());
            userInfoDTO.setFirstName(student.getFirstName());
            userInfoDTO.setLastName(student.getLastName());
        }

        return userInfoDTO;
    }

    public User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
