package com.school_management.api.config;

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
        // Defines which roles has access to which endpoints
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/users/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .requestMatchers("/api/v1/users", "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/teachers/me").hasAnyRole( "TEACHER") // only TEACHER role can access
                .requestMatchers("/api/v1/teachers", "/api/v1/teachers/**").hasRole("ADMIN") // ADMIN can access any other "/teachers" path except "/teachers/me
                .requestMatchers("/api/v1/students/me").hasRole( "STUDENT") // only STUDENT role can access
                .requestMatchers("/api/v1/students", "/api/v1/students/**").hasRole("ADMIN") // ADMIN can access any other "/students" path except "/students/me
                .requestMatchers("/api/v1/courses", "/api/v1/courses/**").hasRole("ADMIN") // ADMIN can access any other "/students" path except "/students/me
                .requestMatchers("/api/v1/enrollment", "/api/v1/enrollment/**").hasRole("ADMIN") // ADMIN can access any other "/students" path except "/students/me
                .anyRequest().authenticated());

        // disables csrfvb
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
