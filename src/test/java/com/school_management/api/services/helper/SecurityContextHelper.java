package com.school_management.api.services.helper;

import com.school_management.api.entities.User;
import org.junit.jupiter.api.AfterEach;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class SecurityContextHelper {
    protected void mockSecurityContextUser(User user) {

        // Create a mock Authentication and put it in the Security Context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    public void clearSecurityContext(){
        SecurityContextHolder.clearContext();
    }
}
