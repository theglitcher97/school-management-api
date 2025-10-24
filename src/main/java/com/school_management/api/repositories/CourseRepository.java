package com.school_management.api.repositories;

import com.school_management.api.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findByTeacherId(Long id);
    @Query("""
            SELECT e.course FROM Enrollment e
            WHERE e.student.id = :studentId
            """)
    List<Course> findByStudentId(@Param("studentId") Long id);

    List<Course> findByNameLike(String term);

    List<Course> findByNameOrDescriptionContaining(String term, String term2);
}
