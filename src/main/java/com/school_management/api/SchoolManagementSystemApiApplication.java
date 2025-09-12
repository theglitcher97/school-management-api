package com.school_management.api;

import com.school_management.api.entities.User;
import com.school_management.api.repositories.UserRepository;
import com.school_management.api.utils.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SchoolManagementSystemApiApplication {
    private final String ADMIN_EMAIL = "admin@admin.com";
    private final String ADMIN_PASSWORD = "adminadmin";

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementSystemApiApplication.class, args);
	}

    @Bean
    @Transactional
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            userRepository.findByUsername(ADMIN_EMAIL).orElseGet(() -> {
                User admin = new User();
                admin.setUsername(ADMIN_EMAIL); // actually email
                admin.setRole("ROLE_ADMIN");
                admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                admin.setActive(true);
                return userRepository.save(admin);
            });
        };
    }
}
