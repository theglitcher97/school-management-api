package com.school_management.api.services.interfaces;

import com.school_management.api.dto.TeacherDTO;
import com.school_management.api.entities.User;

import java.util.List;

public interface TeacherService {
    TeacherDTO createTeacher(String firstName, String lastName, User newAccount);

    List<TeacherDTO> getAll();

    TeacherDTO getBydId(Long teacherId);
}
