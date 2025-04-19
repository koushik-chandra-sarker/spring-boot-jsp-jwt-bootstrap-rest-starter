package com.example.demo.dto.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private String status;
    private T data;
    
    public ApiResponse(String message, T data) {
        this.message = message;
        this.status = "success";
        this.data = data;
    }
    
}

