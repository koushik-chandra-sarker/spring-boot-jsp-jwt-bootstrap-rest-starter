package com.example.demo.controller;

import com.example.demo.dto.payload.request.AuthRequest;
import com.example.demo.dto.payload.request.RefreshTokenRequest;
import com.example.demo.dto.payload.response.AuthResponse;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.RefreshTokenService;
import com.example.demo.service.UserService;
import com.example.demo.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    
    @Value("${app.auth.token.expiration.access}")
    private int accessTokenExpirationSeconds;
    @Value("${app.auth.token.expiration.refresh}")
    private int refreshTokenExpirationSeconds;

    public AuthRestController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
                              RefreshTokenService refreshTokenService, UserService userService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
	    this.userDetailsService = userDetailsService;
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest,
                                              HttpServletRequest request, HttpServletResponse response) {
        String requestUserAgent = request.getHeader("User-Agent");
        String requestIp = request.getRemoteAddr();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUsername(), requestUserAgent, requestIp);
        
        // 🧁 Set Access Token in HttpOnly cookie]
        CookieUtils.addCookie(response, "access_token", jwt);
        
        // 🧁 Set Refresh Token in HttpOnly cookie\
        CookieUtils.addCookie(response, "refresh_token", refreshToken.getToken());
        
        // 📝 Still return body (optional - frontend might still need it)
        return ResponseEntity.ok(new AuthResponse(jwt, refreshToken.getToken(), "Bearer", loginRequest.getUsername()));
    }
    
    @PostMapping("/refreshtoken")
    @ResponseBody
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        return ResponseEntity.ok(refreshTokenService.refreshToken(requestRefreshToken));
    }

    

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        // Set default role as USER
        user.setRoles(Arrays.asList("ROLE_USER"));
        userService.createUser(user);
        return "redirect:/auth/login?registered";
    }
}