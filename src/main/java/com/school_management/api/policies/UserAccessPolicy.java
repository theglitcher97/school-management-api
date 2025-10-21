package com.school_management.api.policies;

import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;


@Component
public class UserAccessPolicy {
    @SneakyThrows
    public static void assertCanCreateUser(User user)  {
        if(!isAdmin(user))
           throw new AccessDeniedException("NOT ALLOWED");
    }

    public static boolean isAdmin(User user){
        return USER_ROLE.valueOf(user.getRole()).equals(USER_ROLE.ROLE_ADMIN);
    }

    @SneakyThrows
    public static void assertCanCreateCourse(User user) {
        if(!isAdmin(user))
            throw new AccessDeniedException("NOT ALLOWED");
    }
}
