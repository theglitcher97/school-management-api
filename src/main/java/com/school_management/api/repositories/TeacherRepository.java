package com.school_management.api.repositories;

import com.school_management.api.dto.TeacherDTO;
import com.school_management.api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query(value = "select t.code from Teacher t order by t.id desc limit 1")
    String getLatestTeacherCode();

    @Query("select new com.school_management.api.dto.TeacherDTO(t.id, u.username, t.firstName, t.lastName, t.code) from Teacher t Join User u on t.id = u.id")
    List<TeacherDTO> findAllWithEmail();

    @Query("select new com.school_management.api.dto.TeacherDTO(t.id, u.username, t.firstName, t.lastName, t.code) from Teacher t Join User u on t.id = u.id where t.id = :teacherId")
    Optional<TeacherDTO> findByIdWithEmail(@Param("teacherId") Long id);
}
