package com.school_management.api.entities;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
    @Id
    private Long id; // same as User.id

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false, length = 64)
    private String firstName;

    @Column(nullable = false, length = 64)
    private String lastName;

    @Column(nullable = false, unique = true, length = 10)
    private String code;
}
