package ca.buildsystem.communication.config;

import ca.buildsystem.communication.exception.CustomAccessDeniedHandler;
import ca.buildsystem.communication.exception.CustomAuthenticationEntryPoint;
import ca.buildsystem.communication.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the Communication Service.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Assuming JwtAuthenticationFilter is implemented similarly to the reports service
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF as we are using JWT
            .authorizeHttpRequests(auth -> auth
                // Allow public access to Swagger UI and API docs
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                // Secure notification endpoints - adjust roles as needed
                .requestMatchers(HttpMethod.POST, "/api/v1/notifications").hasAnyRole("ADMIN", "SYSTEM") // Allow sending only by ADMIN or internal SYSTEM calls
                .requestMatchers(HttpMethod.GET, "/api/v1/notifications/**").hasAnyRole("ADMIN", "SUPPORT") // Allow viewing by ADMIN or SUPPORT
                // Secure other endpoints if any
                .anyRequest().authenticated() // Secure all other endpoints
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless sessions
            )
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
            )
            // Add the JWT filter before the standard UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Define beans for PasswordEncoder, AuthenticationProvider, AuthenticationManager if needed
    // (Likely not needed if authentication is handled by an external service and only validation happens here)
}

