package com.deinterlab.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Configure CORS settings
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()  // Allow unauthenticated access to /auth/**
                        .anyRequest().authenticated()             // Require authentication for all other requests
                )
                .csrf(AbstractHttpConfigurer::disable);       // Disable CSRF if not needed (optional)

        return http.build();
    }

    // Configure CORS settings
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Specify allowed origins. You can add multiple origins or use "*" for all origins (use cautiously).
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));  // Frontend's origin

        // Specify allowed methods
        corsConfiguration.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));  // Allow OPTIONS method for preflight requests

        // Specify allowed headers
        corsConfiguration.setAllowedHeaders(List.of("*"));  // Allow all headers

        // Allow credentials (cookies, HTTP authentication)
        corsConfiguration.setAllowCredentials(true);

        // Handle preflight request properly
        corsConfiguration.setMaxAge(3600L);  // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);  // Apply CORS configuration to all endpoints

        return source;
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/auth/**");                // Ignore URLs starting with /auth/
//    }
}
