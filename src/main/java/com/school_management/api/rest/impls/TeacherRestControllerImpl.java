package com.school_management.api.rest.impls;

import com.school_management.api.dto.TeacherDTO;
import com.school_management.api.rest.interfaces.TeacherRestController;
import com.school_management.api.services.interfaces.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TeacherRestControllerImpl implements TeacherRestController {
    private final TeacherService teacherService;

    @Override
    public ResponseEntity<List<TeacherDTO>> getAllTeachers() {
        return new ResponseEntity<>(this.teacherService.getAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TeacherDTO> getCurrentLoggedInTeacher() {
        return null;
    }

    @Override
    public ResponseEntity<TeacherDTO> getTeacherByID(Long teacherId) {
        return null;
    }
}
