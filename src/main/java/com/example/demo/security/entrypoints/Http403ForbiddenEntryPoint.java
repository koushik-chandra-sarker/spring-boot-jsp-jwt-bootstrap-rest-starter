package com.example.demo.security.entrypoints;

import com.example.demo.dto.payload.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class Http403ForbiddenEntryPoint implements AccessDeniedHandler {
    
    @Override
    
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String uri = request.getRequestURI();
        String contentType = request.getContentType();
        String method = request.getMethod();
        String xRequestedWith = request.getHeader("X-Requested-With");
        boolean isApiRequest = uri.startsWith("/api") ||
//                uri.startsWith("/auth") && !"GET".equalsIgnoreCase(method) ||
                (contentType != null && contentType.contains("application/json")) ||
                "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
        if (isApiRequest) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse("Insufficient privileges", "FORBIDDEN")));
        } else {
            response.sendRedirect("/access-denied");
        }
    }
}