package com.switchfully.eurder.security;

import com.switchfully.eurder.domain.Role;
import com.switchfully.eurder.repository.CustomerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    public static final String ADMIN_EMAIL = "admin@eurder.com";
    public static final String ADMIN_PASSWORD = "admin123";

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(CustomerRepository customerRepository,
                                                 PasswordEncoder passwordEncoder) {
        String encodedAdminPassword = passwordEncoder.encode(ADMIN_PASSWORD);

        return email -> {
            if (ADMIN_EMAIL.equals(email)) {
                return User.withUsername(ADMIN_EMAIL)
                        .password(encodedAdminPassword)
                        .roles(Role.ADMIN.name())
                        .build();
            }
            return customerRepository.getByEmail(email)
                    .map(customer -> User.withUsername(customer.getEmail())
                            .password(customer.getPassword())
                            .roles(Role.CUSTOMER.name())
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("Unknown user: " + email));
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(basic -> basic.authenticationEntryPoint(
                        (request, response, exception) -> handleUnauthorized(request, response)))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) -> handleUnauthorized(request, response))
                        .accessDeniedHandler((request, response, exception) -> handleForbidden(request, response)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/customers").permitAll()
                        .requestMatchers(HttpMethod.POST, "/eurders", "/eurders/*/reorder").hasRole(Role.CUSTOMER.name())
                        .requestMatchers(HttpMethod.GET, "/customers", "/customers/*").hasRole(Role.ADMIN.name())
                        .requestMatchers("/items", "/items/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    private void handleUnauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.warn("Unauthorized access: {} {}", request.getMethod(), request.getRequestURI());
        response.setHeader("WWW-Authenticate", "Basic realm=\"eurder\"");
        writeError(response, 401, "Authentication required");
    }

    private void handleForbidden(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.warn("Forbidden access: {} {}", request.getMethod(), request.getRequestURI());
        writeError(response, 403, "Access denied");
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}