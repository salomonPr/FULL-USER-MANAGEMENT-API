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
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService userDetailsService;


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

                        // private endpoints this is for user only
                        .requestMatchers(HttpMethod.PATCH,"/api/users/patchUpdates/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.PUT,"/api/users/getUpdates/**").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/deleteUpdates/**").hasAnyRole("ADMIN","USER")

                        // private endpoints this is for admin only
                        .requestMatchers(HttpMethod.GET,"/api/users/getAllUsers").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/getUserById/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/age/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/username/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/address/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/users/updateUserById/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH,"/api/users/patchUserById/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/deleteUserById/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                );
        return http.build();

    }

}
