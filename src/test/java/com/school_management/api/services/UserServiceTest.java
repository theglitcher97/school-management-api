package com.school_management.api.services;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.dto.UserInfoDTO;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.helper.SecurityContextHelper;
import com.school_management.api.services.impls.UsersServiceImpl;
import com.school_management.api.services.interfaces.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends SecurityContextHelper {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private UsersServiceImpl usersService;

    @Test
    public void shouldCreateAccountHappyPath(){

    }

    @Test
    public void shouldReturnUserInfoByIdIfExists(){
        // Arrange
        User user = User.builder().id(1L).username("test@gmail.com").role(USER_ROLE.STUDENT.getValue()).build();
        StudentDTO student = StudentDTO.builder().firstName("John").lastName("Doe").id(1L).build();
        mockSecurityContextUser(user);

        // Act
        when(this.studentService.getBydId(anyLong())).thenReturn(student);
        UserInfoDTO userInfoDTO = this.usersService.getCurrentUserInfo();

        // assess
        assertEquals(userInfoDTO.getEmail(), user.getUsername());
        verify(this.studentService, atLeastOnce()).getBydId(anyLong());
    }
}
