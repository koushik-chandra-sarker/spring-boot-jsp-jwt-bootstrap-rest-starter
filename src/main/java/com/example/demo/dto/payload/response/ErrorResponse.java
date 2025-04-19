package com.example.demo.dto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
    private String error;
    private String timestamp;
    
    public ErrorResponse(String message, String error) {
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now().toString();
    }
    
    
}
