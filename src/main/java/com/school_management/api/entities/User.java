package com.school_management.api.entities;

import com.school_management.api.enums.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String username;

    @Column(nullable = false, length = 50)
    private String role;

    @Column(nullable = false)
    private Boolean active = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @PrePersist
    private void prePersist(){
        this.setActive(true);
        this.validateRole();
    }

    private void validateRole(){
        if (Arrays.stream(USER_ROLE.values()).noneMatch(role -> role.getValue().equals(this.role)))
            throw new RuntimeException("Role not valid exception");
    }
}

