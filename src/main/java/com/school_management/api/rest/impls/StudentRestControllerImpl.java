package com.school_management.api.rest.impls;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.rest.interfaces.StudentRestController;
import com.school_management.api.services.interfaces.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class StudentRestControllerImpl implements StudentRestController {
    private final StudentService studentService;

    @Override
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return new ResponseEntity<>(this.studentService.getAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StudentDTO> getStudentByID(Long studentId) {
        return new ResponseEntity<StudentDTO>(this.studentService.getBydId(studentId), HttpStatus.OK);
    }
}
