package com.example.demo.security.filters;


import com.example.demo.dto.payload.response.AuthResponse;
import com.example.demo.dto.payload.response.ErrorResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.CustomUserService;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.utils.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//    @Value("${app.auth.token.expiration.access}")
//    private int accessTokenExpirationSeconds;
    @Autowired
    ApplicationContext context;
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
   /* @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/home") || path.startsWith("/auth");
    }*/
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Inside JwtAuthenticationFilter");
        String accessToken = resolveToken(request);
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }
        // Refresh token if access token is expired
        if (tokenProvider.isTokenExpired(accessToken)) {
            log.warn("Access token expired");
            // Try refreshing token for cookies based request only
            String refreshToken = resolveRefreshToken(request);
            log.info("refreshToken: {}", refreshToken);
            if (StringUtils.hasText(refreshToken) ) {
                log.info("Refreshing token");
                try {
                    RefreshTokenService refreshTokenService = context.getBean(RefreshTokenService.class);
                    
                    AuthResponse refreshResponse = refreshTokenService.refreshToken(refreshToken);
                    log.info("Refreshing: authResponse: {}", refreshResponse);
                    // Generate new access token
                    String newAccessToken = refreshResponse.getAccessToken();
                    accessToken = newAccessToken;
                    UserDetails userDetails =
                            context.getBean(CustomUserService.class).loadUserByUsername(refreshResponse.getUsername());
                    // Update security context
                    setAuthContext(userDetails, request);
                    
                    // Add new access token to response cookie
                    CookieUtils.addCookie(response, "access_token", newAccessToken);
                    
                    log.info("Access token refreshed successfully.");
                    
                } catch (Exception ex) {
                    log.warn("Refresh token failed: {}", ex.getMessage());
                    log.warn("Removing cookies");
                    CookieUtils.deleteAllCookies(request, response);
                    if (isRestCall(request)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(ex.getMessage(), "UNAUTHORIZED")));
                        return;
                    }
                }
            }
        }
        
        // Validate access token and set security context
        String username = null;
        if (StringUtils.hasText(accessToken)) {
            try {
                username = tokenProvider.extractUserName(accessToken);
            } catch (Exception ex) {
                log.warn("Access token invalid or expired");
                if (isRestCall(request)) {
                    ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "UNAUTHORIZED");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
                    return;
                }
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("Inside if authentication null");
            UserDetails userDetails = context.getBean(CustomUserService.class).loadUserByUsername(username);
            if (tokenProvider.validateToken(accessToken, userDetails)) {
                setAuthContext(userDetails, request);
            }
        }

        filterChain.doFilter(request, response);
    }
    
    private boolean isRestCall(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        String requestURI = request.getRequestURI();
        String contentType = request.getContentType();
        String xRequestedWith = request.getHeader("X-Requested-With");
        return acceptHeader != null && acceptHeader.contains("application/json")
                || requestURI.startsWith("/api")
                || contentType != null && contentType.contains("application/json")
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith);
    }
    private void setAuthContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
    
    private String resolveToken(HttpServletRequest request) {
        // First check Authorization header for API Requests
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        // Then fallback to cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    private String resolveRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}