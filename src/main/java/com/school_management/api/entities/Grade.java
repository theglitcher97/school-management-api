package com.school_management.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "grades",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"enroll_id", "assessment_id"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "enroll_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @Column(nullable = false, precision = 3)
    private Double score;

    @Column(nullable = false)
    private LocalDate gradedDate;
}


