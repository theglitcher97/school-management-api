package com.school_management.api.config;

import com.school_management.api.enums.USER_TYPE;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class AppConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        String ADMIN = USER_TYPE.ADMIN.getValue();
        String TEACHER = USER_TYPE.TEACHER.getValue();
        String STUDENT = USER_TYPE.STUDENT.getValue();

        // Defines what ROLE can access which resource, not what data can see
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/users/**").hasAnyRole(ADMIN, TEACHER, STUDENT)
                .requestMatchers("/api/v1/teachers/**").hasAnyRole(ADMIN, TEACHER)
                .requestMatchers("/api/v1/students/**").hasRole(ADMIN)
                .requestMatchers("/api/v1/courses/**").hasAnyRole(ADMIN, TEACHER, STUDENT)
                .requestMatchers("/api/v1/enrollment/**").hasRole(ADMIN)
                .anyRequest().authenticated());

        // disables csrf
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // cors configuration
        httpSecurity.cors(cors -> cors.configurationSource(configurationSource()));

        // some extra config
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authenticationProvider(this.authenticationProvider);
        httpSecurity.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Cors configuration for the server, this specified who can connect to the server,
     * among other things
     * @return
     */
    @Bean
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*")); // allow headers (consider restricting for security)
        configuration.setAllowCredentials(true); // allow sending credential (cookies, auth headers
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // apply cors to all paths
        return source;
    }
}
