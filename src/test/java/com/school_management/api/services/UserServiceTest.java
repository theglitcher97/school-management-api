package com.school_management.api.services;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.dto.UserInfoDTO;
import com.school_management.api.entities.Student;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.impls.UsersServiceImpl;
import com.school_management.api.services.interfaces.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private UsersServiceImpl usersService;

    @Test
    public void shouldReturnUserInfoByIdIfExists(){
        // Arrange
        User user = User.builder().id(1L).role(USER_ROLE.STUDENT.getValue()).build();
        StudentDTO student = StudentDTO.builder().id(1L).build();
        UserInfoDTO userInfoDTO = new UserInfoDTO(user.getUsername(), user.getRole(), student.getFirstName(), student.getFirstName());

        // Act
        when(this.usersService.getCurrentUser()).thenReturn(user);
        when(this.studentService.getBydId(anyLong())).thenReturn(student);
        when(this.usersService.getCurrentUserInfo()).thenReturn(userInfoDTO);

        // assess
        assertEquals(userInfoDTO.getEmail(), user.getUsername());
    }
}
