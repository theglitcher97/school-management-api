package com.school_management.api.repositories;

import com.school_management.api.entities.Student;
import com.school_management.api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "select s.code from Student s order by s.id desc limit 1")
    String getLatestStudentCode();
}
