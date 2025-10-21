package com.school_management.api.rest.interfaces;

import com.school_management.api.dto.TeacherDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1/teachers")
public interface TeacherRestController {
    @GetMapping
    ResponseEntity<List<TeacherDTO>> getAllTeachers();

    /**
     * todo: remove
     * @deprecated should call /api/v1/users/me
     * */
    @GetMapping("/me")
    ResponseEntity<TeacherDTO> getCurrentLoggedInTeacher();

    @GetMapping("/{teacherId}")
    ResponseEntity<TeacherDTO> getTeacherByID(@PathVariable Long teacherId);
}
