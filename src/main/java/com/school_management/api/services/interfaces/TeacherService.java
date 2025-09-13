package com.school_management.api.services.interfaces;

import com.school_management.api.dto.TeacherDTO;
import com.school_management.api.entities.User;

public interface TeacherService {
    TeacherDTO createTeacher(String firstName, String lastName, User newAccount);
}
