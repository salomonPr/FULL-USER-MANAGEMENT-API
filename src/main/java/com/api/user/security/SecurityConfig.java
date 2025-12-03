package com.api.user.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf-> csrf.disable())
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth
                        // public endpoints
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()

                        // user/admin endpoints matching actual controller mappings
                        .requestMatchers(HttpMethod.GET, "/api/users/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/*").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/users/username/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/users/address/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/users/age/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/users/email/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/api/users/phoneNumber/**").hasAnyRole("ADMIN","USER")

                        .requestMatchers(HttpMethod.PUT, "/api/users/updateUsers/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/patchUpdates/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/delete/**").hasAnyRole("ADMIN","USER")
                        .anyRequest().authenticated()
                );

        // register JWT filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

}
