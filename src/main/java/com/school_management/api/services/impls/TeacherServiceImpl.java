package com.school_management.api.services.impls;

import com.school_management.api.dto.TeacherDTO;
import com.school_management.api.entities.Teacher;
import com.school_management.api.entities.User;
import com.school_management.api.repositories.TeacherRepository;
import com.school_management.api.services.interfaces.TeacherService;
import com.school_management.api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final String INIT_CODE = "T0000001";
    private final TeacherRepository teacherRepository;

    @Override
    @Transactional
    public TeacherDTO createTeacher(String firstName, String lastName, User user) {
        String latestTeacherCode = this.teacherRepository.getLatestTeacherCode();

        Teacher teacher =
                Teacher.builder()
                        .firstName(firstName)
                        .lastName(firstName)
                        .code(Objects.isNull(latestTeacherCode) ? INIT_CODE : AppUtils.getNextCode(latestTeacherCode))
                        .user(user)
                        .build();

        teacher = this.teacherRepository.save(teacher);
        return new TeacherDTO(teacher.getFirstName(), teacher.getLastName(), teacher.getCode());
    }
}
