package com.school_management.api.services;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.dto.UserInfoDTO;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.services.impls.UsersServiceImpl;
import com.school_management.api.services.interfaces.StudentService;
import com.school_management.api.services.interfaces.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
        User user = User.builder().id(1L).username("test@gmail.com").role(USER_ROLE.STUDENT.getValue()).build();
        StudentDTO student = StudentDTO.builder().firstName("John").lastName("Doe").id(1L).build();

        // Create a mock Authentication and put it in the Security Context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        when(this.studentService.getBydId(anyLong())).thenReturn(student);
        UserInfoDTO userInfoDTO = this.usersService.getCurrentUserInfo();

        // assess
        assertEquals(userInfoDTO.getEmail(), user.getUsername());
        verify(this.studentService, atLeastOnce()).getBydId(anyLong());
    }
}
