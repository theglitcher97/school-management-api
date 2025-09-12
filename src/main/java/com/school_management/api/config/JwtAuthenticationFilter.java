package com.school_management.api.config;

import com.google.gson.Gson;
import com.school_management.api.services.interfaces.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Configuration
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final String BEARER_PREFIX = "Bearer";
    private final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    Gson gson;

    /**
     * Each request wil go through this filter to check if it has a token and if it does,
     * validates if the token is valid
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // extract auth header
        final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        // if not auth header, let the request continue
        if (Objects.isNull(authorizationHeader) || Strings.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        //extract user info from token
        final String token = authorizationHeader.substring(BEARER_PREFIX.length() + 1);
        final String email = this.jwtService.extractEmail(token);

        // if user is not authenticated yet, then set the authentication into the app security context
        if (Objects.nonNull(email) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);

            // validate token
            if (this.jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
