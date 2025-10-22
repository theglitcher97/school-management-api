package com.school_management.api.policies;

import com.school_management.api.entities.Course;
import com.school_management.api.entities.User;
import com.school_management.api.enums.USER_ROLE;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Objects;


@Component
public class UserAccessPolicy {
    @SneakyThrows
    public static void assertCanCreateUser(User user) {

    }

    public static boolean isAdmin(User user){
        return USER_ROLE.valueOf(user.getRole()).equals(USER_ROLE.ROLE_ADMIN);
    }

    @SneakyThrows
    public static void assertCanCreateCourse(User user) {
        if(!isAdmin(user))
            throw new AccessDeniedException("NOT ALLOWED");
    }

    @SneakyThrows
    public void assertCanCreateEnroll(User user) {
        if(!isAdmin(user))
            throw new AccessDeniedException("NOT ALLOWED");
    }

    @SneakyThrows
    public void assertCanReadCourse(User user, Course course) {
        if (isAdmin(user)) return;
        if (isTeacher(user) && Objects.equals(course.getTeacher().getId(), user.getId())) return;
        if (isStudent(user) && course.getEnrollments().stream().anyMatch(enrollment -> Objects.equals(enrollment.getStudent().getId(), user.getId())))
           return;

        throw new AccessDeniedException("NOT ALLOWED");
    }

    private boolean isTeacher(User user) {
        return USER_ROLE.valueOf(user.getRole()).equals(USER_ROLE.ROLE_TEACHER);
    }

    private boolean isStudent(User user) {
        return USER_ROLE.valueOf(user.getRole()).equals(USER_ROLE.ROLE_STUDENT);
    }

    @SneakyThrows
    public void assertCanReadStudent(User user) {
        if(!isAdmin(user))
            throw new AccessDeniedException("NOT ALLOWED");
    }

    @SneakyThrows
    public void assertCanReadCourseStudents(User user, Course course) {
        if (isAdmin(user)) return;
        if (isTeacher(user) && Objects.equals(course.getTeacher().getId(), user.getId())) return;
        throw new AccessDeniedException("NOT ALLOWED");
    }

    @SneakyThrows
    public void assertCanDeleteCourse(User user) {
        if(!isAdmin(user))
            throw new AccessDeniedException("NOT ALLOWED");
    }
}
