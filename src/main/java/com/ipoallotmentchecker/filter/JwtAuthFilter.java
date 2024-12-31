package com.ipoallotmentchecker.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ipoallotmentchecker.config.JwtService;
import com.ipoallotmentchecker.config.UserInfoService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserInfoService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        
        String requestURI = request.getRequestURI();
        // Skip JWT authentication for Swagger UI and related URLs
        if (requestURI.startsWith("/auth/addNewUser") ||requestURI.startsWith("/auth/generateToken") ||    requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/swagger-ui/") ||
            requestURI.startsWith("/swagger-resources/") || requestURI.startsWith("/webjars/") ||
            requestURI.startsWith("/auth/generateToken")) {
            filterChain.doFilter(request, response); // Skip filter and continue the chain
            return;
        }
        // Check if the header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Extract token
            username = jwtService.extractUsername(token); // Extract username from token
        }

        // If no token or invalid token
        if (token == null || username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status to 401
            response.getWriter().write("{\"message\": \"Unauthorized access. Please provide a valid token.\"}");
            return; // Return early without continuing the filter chain
        }

        // Validate the token and check if it is expired
        if (jwtService.isTokenExpired(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set status to 401
            response.getWriter().write("{\"message\": \"Token has expired. Please provide a valid token.\"}");
            return; // Return early without continuing the filter chain
        }

        // If the token is valid and no authentication is set in the context
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token and set authentication
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain if token is valid
        filterChain.doFilter(request, response);
    }
}
