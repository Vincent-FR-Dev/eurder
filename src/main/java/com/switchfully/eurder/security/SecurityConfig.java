package com.switchfully.eurder.security;

import com.switchfully.eurder.repository.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public static final String ADMIN_EMAIL = "admin@eurder.com";
    public static final String ADMIN_PASSWORD = "admin123";

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
                        .roles("ADMIN")
                        .build();
            }
            return customerRepository.getByEmail(email)
                    .map(customer -> User.withUsername(customer.getEmail())
                            .password(customer.getPassword())
                            .roles("CUSTOMER")
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("Unknown user: " + email));
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // ouverts à tous
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/customers").permitAll()
                        // customer
                        .requestMatchers(HttpMethod.POST, "/eurders", "/eurders/*/reorder").hasRole("CUSTOMER")
                        // admin
                        .requestMatchers(HttpMethod.GET, "/customers", "/customers/*").hasRole("ADMIN")
                        .requestMatchers("/items", "/items/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}