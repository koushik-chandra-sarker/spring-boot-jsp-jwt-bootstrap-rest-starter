package com.example.demo.security.entrypoints;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        
        String uri = request.getRequestURI();
        String contentType = request.getContentType();
        String method = request.getMethod();
        String xRequestedWith = request.getHeader("X-Requested-With");
        
        boolean isApiRequest = uri.startsWith("/api") ||
//                uri.startsWith("/auth") && !"GET".equalsIgnoreCase(method) ||
                (contentType != null && contentType.contains("application/json")) ||
                "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
        
        if (isApiRequest) {
            // For REST/API clients
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized or token expired\"}");
        } else {
            // For browser form-based login
            response.sendRedirect("/auth/login");
        }
        /*if(!isApiRequest){
            response.sendRedirect("/auth/login");
        }*/
    }
}