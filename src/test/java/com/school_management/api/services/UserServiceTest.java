package com.school_management.api.services;

import com.school_management.api.dto.*;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.enums.USER_TYPE;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.helper.SecurityContextHelper;
import com.school_management.api.services.impls.UsersServiceImpl;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.services.interfaces.TeacherService;
import com.school_management.api.utils.CurrentUserProvider;
import com.school_management.api.utils.PasswordGenerator;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends SecurityContextHelper {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private UsersServiceImpl usersService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Test
    public void shouldCreateStudentAccountHappyPath(){
        // Arrange
        CreateUserAccountDTO newAccount = CreateUserAccountDTO.builder()
                .email("test@gmail.com")
                .firstName("john")
                .lastName("doe")
                .userType(USER_TYPE.STUDENT)
                .build();

        // Act
        when(this.currentUserProvider.getCurrentUser()).thenReturn(this.getDefaultAdminUser());
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(this.passwordGenerator.generatePassword(anyInt())).thenReturn("password_generated");
        when(this.passwordEncoder.encode(anyString())).thenReturn("password_encoded");
        when(this.userRepository.save(any(User.class))).thenReturn(User.builder().id(1L).username("test@gmail.com").build());
        when(this.studentService.createStudent(anyString(), anyString(), any(User.class))).thenReturn(StudentDTO.builder().code("S00001").build());

        AccountCreatedDTO accountCreatedDTO = this.usersService.createAccount(newAccount);
        assertEquals("S00001", accountCreatedDTO.getCode());
        verify(this.studentService, atLeastOnce()).createStudent(anyString(), anyString(), any(User.class));
    }

    private User getDefaultAdminUser() {
        return User.builder()
                .id(1L)
                .role(USER_ROLE.ROLE_ADMIN.getValue())
                .build();
    }

    @Test
    public void shouldCreateTeacherAccountHappyPath(){
        // Arrange
        CreateUserAccountDTO newAccount = CreateUserAccountDTO.builder()
                .email("test@gmail.com")
                .firstName("john")
                .lastName("doe")
                .userType(USER_TYPE.TEACHER)
                .build();

        // Act
        when(this.currentUserProvider.getCurrentUser()).thenReturn(this.getDefaultAdminUser());
        when(this.userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(this.passwordGenerator.generatePassword(anyInt())).thenReturn("password_generated");
        when(this.passwordEncoder.encode(anyString())).thenReturn("password_encoded");
        when(this.userRepository.save(any(User.class))).thenReturn(User.builder().id(1L).username("test@gmail.com").build());
        when(this.teacherService.createTeacher(anyString(), anyString(), any(User.class))).thenReturn(TeacherDTO.builder().code("T00001").build());

        AccountCreatedDTO accountCreatedDTO = this.usersService.createAccount(newAccount);
        assertEquals("T00001", accountCreatedDTO.getCode());
        verify(this.teacherService, atLeastOnce()).createTeacher(anyString(), anyString(), any(User.class));
    }

    @Test
    public void shouldNotCreateAccountIfEmailAlreadyExists(){
        // Arrange
        CreateUserAccountDTO newAccount = CreateUserAccountDTO.builder()
                .email("test@gmail.com")
                .firstName("john")
                .lastName("doe")
                .userType(USER_TYPE.STUDENT)
                .build();

        // Act
        when(this.currentUserProvider.getCurrentUser()).thenReturn(this.getDefaultAdminUser());
        when(this.userRepository.findByUsername(anyString())).thenThrow(new EntityExistsException("This email already exists"));
        EntityExistsException exception = assertThrows(EntityExistsException.class, () -> this.usersService.createAccount(newAccount));
        assertEquals("This email already exists", exception.getMessage());
    }

    @Test
    public void shouldReturnUserStudentInfoByIdIfExists(){
        // Arrange
        User user = User.builder().id(1L).username("test@gmail.com").role(USER_ROLE.ROLE_STUDENT.getValue()).build();
        StudentDTO student = StudentDTO.builder().firstName("John").lastName("Doe").id(1L).build();
//        mockSecurityContextUser(user);

        // Act
        when(this.currentUserProvider.getCurrentUser()).thenReturn(user);
        when(this.studentService.getBydId(anyLong())).thenReturn(student);
        UserInfoDTO userInfoDTO = this.usersService.getCurrentUserInfo();

        // assess
        assertEquals(userInfoDTO.getEmail(), user.getUsername());
        verify(this.studentService, atLeastOnce()).getBydId(anyLong());
    }

    @Test
    public void shouldReturnUserTeacherInfoByIdIfExists(){
        // Arrange
        User user = User.builder().id(1L).username("test@gmail.com").role(USER_ROLE.ROLE_TEACHER.getValue()).build();
        TeacherDTO teacher = TeacherDTO.builder().firstName("John").lastName("Doe").id(1L).build();
//        mockSecurityContextUser(user);

        // Act
        when(this.teacherService.getBydId(anyLong())).thenReturn(teacher);
        when(this.currentUserProvider.getCurrentUser()).thenReturn(user);
        UserInfoDTO userInfoDTO = this.usersService.getCurrentUserInfo();

        // assess
        assertEquals(userInfoDTO.getEmail(), user.getUsername());
        verify(this.teacherService, atLeastOnce()).getBydId(anyLong());
    }

    @Test
    public void shouldReturnUserAdminInfoByIdIfExists(){
        // Arrange
        User user = User.builder().id(1L).username("test@gmail.com").role("ROLE_ADMIN").build();
//        mockSecurityContextUser(user);

        // Act
        when(this.currentUserProvider.getCurrentUser()).thenReturn(user);
        UserInfoDTO userInfoDTO = this.usersService.getCurrentUserInfo();

        // assess
        assertEquals(userInfoDTO.getEmail(), user.getUsername());
        verify(this.teacherService, never()).getBydId(anyLong());
        verify(this.studentService, never()).getBydId(anyLong());
    }

    @Test
    public void shouldResetPasswordIfUserExists(){
        // Arrange
        User user = User.builder().id(1L).username("test@gmail.com").role(USER_ROLE.ROLE_TEACHER.getValue()).build();

        // Act
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(this.passwordGenerator.generatePassword(anyInt())).thenReturn("password_generated");
        when(this.passwordEncoder.encode(anyString())).thenReturn("password_encoded");
        when(this.userRepository.save(any(User.class))).thenReturn(user);
        String newPassword = this.usersService.resetPassword(1L);


        assertEquals("password_generated", newPassword);
        verify(this.passwordGenerator, atLeastOnce()).generatePassword(anyInt());
        verify(this.passwordEncoder, atLeastOnce()).encode(anyString());
    }

    @Test
    public void shouldResetPasswordFailsIfUserDoesNotExists(){
        // Arrange
        User user = User.builder().id(1L).username("test@gmail.com").role(USER_ROLE.ROLE_TEACHER.getValue()).build();

        // Act
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.usersService.resetPassword(1L));
        verify(this.passwordGenerator, never()).generatePassword(anyInt());
        verify(this.passwordEncoder, never()).encode(anyString());
    }
}
