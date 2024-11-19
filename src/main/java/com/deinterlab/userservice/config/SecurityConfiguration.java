package com.deinterlab.userservice.config;

import com.deinterlab.userservice.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
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
                .csrf(csrf -> csrf.ignoringRequestMatchers("/**")) // Disable CSRF protection for all endpoints
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Apply CORS settings
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()      // Allow all requests to authentication endpoints
                        .requestMatchers("/h2-console/**").permitAll() // Allow all requests to H2 console
                        .anyRequest().authenticated()                // Secure all other requests
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // Allow H2 console in iframes (same-origin policy)

        return http.build();
    }



    // Configure CORS settings
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Specify allowed origins. You can add multiple origins or use "*" for all origins (use cautiously).
        corsConfiguration.setAllowedOrigins(List.of(Constants.DEV_FRONTEND_URL, Constants.PROD_FRONTEND_URL));  // Frontend's origin

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


}
