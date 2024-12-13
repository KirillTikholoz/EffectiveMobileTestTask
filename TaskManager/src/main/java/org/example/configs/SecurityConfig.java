package org.example.configs;

import lombok.RequiredArgsConstructor;
import org.example.filters.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(HttpMethod.POST, "/tasks").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/comments").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/tasks/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/tasks/executor").permitAll()
                                .requestMatchers(HttpMethod.GET, "/tasks/author").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/tasks/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/tasks/*/status").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/tasks/*/priority").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/tasks/*/executor").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/tasks/*").hasRole("ADMIN")
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
