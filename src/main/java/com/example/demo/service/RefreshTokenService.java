package com.example.demo.service;


import com.example.demo.dto.payload.response.AuthResponse;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    @Value("${app.auth.token.expiration.refresh}")
    private Long refreshTokenDurationSeconds;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    
    public RefreshToken createRefreshToken(String username, String requestUserAgent, String requestIp) {
        // Find the user based on the provided username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username " + username));
        
        // Check if the user already has a refresh token
        RefreshToken existingRefreshToken =
                refreshTokenRepository.findByUser_UsernameAndUserAgentAndIpAddress(user.getUsername(), requestUserAgent, requestIp).orElse(null);
        if (existingRefreshToken != null) {
            // If an existing refresh token exists, update its expiry date
            existingRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationSeconds * 1000));
            existingRefreshToken.setToken(UUID.randomUUID().toString());
            refreshTokenRepository.save(existingRefreshToken);  // Update the token
            return existingRefreshToken;
        }
        
        // If no existing refresh token exists, create a new one
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationSeconds * 1000));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserAgent(requestUserAgent);
        refreshToken.setIpAddress(requestIp);
        
        refreshToken = refreshTokenRepository.save(refreshToken);  // Save the new token
        return refreshToken;
    }
    
    
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
    
    public AuthResponse refreshToken(String requestRefreshToken) {
        return findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    // Load full user details to avoid ClassCastException
                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                    
                    // Create authentication object with full UserDetails
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    
                    // Generate new JWT token using the authenticated user
                    String jwt = tokenProvider.generateToken(authentication);
                    
                    return new AuthResponse(
                            jwt,
                            requestRefreshToken,
                            "Bearer",
                            user.getUsername());
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
		                "Refresh token is not in database!"));
    }
}