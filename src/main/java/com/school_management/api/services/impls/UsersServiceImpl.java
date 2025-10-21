package com.school_management.api.services.impls;

import com.school_management.api.dto.*;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.enums.USER_TYPE;
import com.school_management.api.policies.UserAccessPolicy;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.interfaces.UsersService;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.services.interfaces.TeacherService;
import com.school_management.api.utils.CurrentUserProvider;
import com.school_management.api.utils.PasswordGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {
    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);
    private final int DEFAULT_PASSWORD_LENGTH = 10;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public AccountCreatedDTO createAccount(CreateUserAccountDTO accountDTO)  {
        User user = this.currentUserProvider.getCurrentUser();
        logger.info("Checking if user {}-{} has permission to create an account", user.getUsername(), user.getRole());
        UserAccessPolicy.assertCanCreateUser(user);

        logger.info(" check if \"email\" is not already registered");
        this.userRepository.findByUsername(accountDTO.getEmail())
                .ifPresent((userDB) -> {
                    throw new EntityExistsException("This email already exists");
                });

        logger.info("creating new account with role {}", accountDTO.getUserType());
        String generatedPassword = this.passwordGenerator.generatePassword(DEFAULT_PASSWORD_LENGTH);
        User newAccount =
                User.builder()
                        .username(accountDTO.getEmail())
                        .role(
                                accountDTO.getUserType() == USER_TYPE.STUDENT
                                        ? USER_ROLE.ROLE_STUDENT.getValue()
                                        : USER_ROLE.ROLE_TEACHER.getValue())
                        .password(this.passwordEncoder.encode(generatedPassword))
                        .build();

        logger.info("Saving account");
        this.userRepository.save(newAccount);

        // creates and save the related entity teacher/student
        String code = "";

        logger.info("Getting the newly created account code");
        switch (accountDTO.getUserType()) {
            case TEACHER ->
                    code = this.teacherService.createTeacher(accountDTO.getFirstName(), accountDTO.getLastName(), newAccount).getCode();
            case STUDENT ->
                    code = this.studentService.createStudent(accountDTO.getFirstName(), accountDTO.getLastName(), newAccount).getCode();
        }

        // todo: send credential to user by email
        logger.info("returning new account info");
        return new AccountCreatedDTO(accountDTO.getEmail(), generatedPassword, code);
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
        User user = this.currentUserProvider.getCurrentUser();
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setEmail(user.getUsername());
        userInfoDTO.setRole(user.getRole().split("_")[1]); // ROLE_X -> X
        userInfoDTO.setFirstName("John");
        userInfoDTO.setLastName("Doe");

        if (user.getRole().equals(USER_ROLE.ROLE_TEACHER.getValue())) {
            TeacherDTO teacher = this.teacherService.getBydId(user.getId());
            userInfoDTO.setFirstName(teacher.getFirstName());
            userInfoDTO.setLastName(teacher.getLastName());
        } else if (user.getRole().equals(USER_ROLE.ROLE_STUDENT.getValue())) {
            StudentDTO student = this.studentService.getBydId(user.getId());
            userInfoDTO.setFirstName(student.getFirstName());
            userInfoDTO.setLastName(student.getLastName());
        }

        return userInfoDTO;
    }
}
