package com.school_management.api.policies;

import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import org.springframework.stereotype.Component;



@Component
public class CourseAccessPolicy {
    public static boolean canReadCourses(User user) {
        return switch (USER_ROLE.valueOf(user.getRole())) {
            case USER_ROLE.ROLE_ADMIN, USER_ROLE.ROLE_STUDENT, USER_ROLE.ROLE_TEACHER -> true;
            default -> false;
        };
    }

    public static boolean canCreateCourses(User user) {
        return switch (USER_ROLE.valueOf(user.getRole())) {
            case USER_ROLE.ROLE_ADMIN -> true;
            default -> false;
        };
    }

    public static boolean canUpdateCourses(User user) {
        return switch (USER_ROLE.valueOf(user.getRole())) {
            case USER_ROLE.ROLE_ADMIN -> true;
            default -> false;
        };
    }
}
