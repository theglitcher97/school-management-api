package com.school_management.api.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "assessments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private LocalDate dueDate;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Grade> grades;
}

