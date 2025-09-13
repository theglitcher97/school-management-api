package com.school_management.api.services.interfaces;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.entities.User;

public interface StudentService {
    StudentDTO createStudent(String firstName, String lastName, User newAccount);
}
