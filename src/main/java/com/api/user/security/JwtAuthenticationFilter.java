//package com.api.user.security;
//
//import com.api.user.entity.Role;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Component
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        try {
//            //step 1: get token from request header
//            String authorization = request.getHeader("Authorization");
//
//            String username = null;
//            String token = null;
//
//            // step 2: Extract token from the "Bearer TOKEN_HERE" format
//            if (authorization != null && authorization.startsWith("Bearer ")) {
//                token = authorization.substring(7);
//                try {
//                    username = jwtUtils.getUsernameFromToken(token);
//                } catch (Exception e) {
//                    // Invalid token, continue without authentication
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//            }
//
//            // step 3: validate token and set authentification
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                if (jwtUtils.validateToken(token, username)) {
//                    // token is valid!
//
//                    // get role from token
//                    Role role = jwtUtils.getRoleFromToken(token);
//
//                    // create an authentification object
//                    UsernamePasswordAuthenticationToken authenticationToken =
//                            new UsernamePasswordAuthenticationToken(username,
//                                    null,
//                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())));
//
//                    // set authentication in spring Security contex
//                    SecurityContextHolder
//                            .getContext()
//                            .setAuthentication(authenticationToken);
//                }
//
//            }
//        } catch (Exception e) {
//            // Log error and continue without authentication
//            logger.error("JWT authentication error: " + e.getMessage());
//        }
//
//        filterChain.doFilter(request, response);
//
//    }
//}
