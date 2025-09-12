package com.school_management.api;

import com.school_management.api.entities.User;
import com.school_management.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SchoolManagementSystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementSystemApiApplication.class, args);
	}

    @Bean
    @Transactional
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            userRepository.findByUsername("admin@admin.com").orElseGet(() -> {
                User admin = new User();
                admin.setUsername("admin@admin.com"); // actually email
                admin.setRole("ADMIN");
                admin.setPassword(passwordEncoder.encode("superadmin"));
                admin.setActive(true);
                return userRepository.save(admin);
            });
        };
    }
}
