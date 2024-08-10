package com.springteam.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private JwtGenerator jwtGenerator;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, AuthenticationEntryPoint authenticationEntryPoint, JwtGenerator jwtGenerator) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtGenerator = jwtGenerator;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/dashboard/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/category/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/category/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/category/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/product/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/product/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/product/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/api/order/orders/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/rating/new-rating").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().permitAll())
                .exceptionHandling(exc -> exc.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthFilter jwtAuthenticationFilter() {
        return new JwtAuthFilter(jwtGenerator, userDetailsService);
    }
}
