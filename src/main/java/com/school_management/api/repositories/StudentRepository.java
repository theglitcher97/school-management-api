package com.school_management.api.repositories;

import com.school_management.api.dto.StudentDTO;
import com.school_management.api.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "select s.code from Student s order by s.id desc limit 1")
    String getLatestStudentCode();

    @Query("select new com.school_management.api.dto.StudentDTO(s.id, u.username, s.firstName, s.lastName, s.code) from Student s Join User u on s.id = u.id")
    List<StudentDTO> findAllWithEmail();

    @Query("select new com.school_management.api.dto.StudentDTO(s.id, u.username, s.firstName, s.lastName, s.code) from Student s Join User u on s.id = u.id where s.id = :studentId")
    Optional<StudentDTO> findByIdWithEmail(@Param("studentId") Long studentId);
}
