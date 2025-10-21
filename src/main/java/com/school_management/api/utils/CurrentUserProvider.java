package com.school_management.api.utils;

import com.school_management.api.entities.User;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Objects;

@Component
public class CurrentUserProvider {

    @SneakyThrows
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication)) throw new AccessDeniedException("No authenticated user found");
        return (User) authentication.getPrincipal();
    }
}
