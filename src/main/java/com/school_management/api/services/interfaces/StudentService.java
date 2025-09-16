package com.school_management.api.services.interfaces;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.entities.Student;
import com.school_management.api.entities.User;

import java.util.List;

public interface StudentService {
    StudentDTO createStudent(String firstName, String lastName, User newAccount);

    List<StudentDTO> getAll();

    StudentDTO getCurrentStudentInfo();

    StudentDTO getBydId(Long studentId);

    StudentDTO entityToDTO(Student student);
}
