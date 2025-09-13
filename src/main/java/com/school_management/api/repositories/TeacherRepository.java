package com.school_management.api.repositories;

import com.school_management.api.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query(value = "select t.code from Teacher t order by t.id desc limit 1")
    String getLatestTeacherCode();
}
