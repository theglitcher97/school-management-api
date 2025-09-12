package com.school_management.api.repositories;

import com.school_management.api.entities.Student;
import com.school_management.api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
