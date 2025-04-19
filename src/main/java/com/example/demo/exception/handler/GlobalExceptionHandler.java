package com.example.demo.exception.handler;

import com.example.demo.dto.payload.response.ErrorResponse;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.InternalServerErrorException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.TokenRefreshException;
import com.example.demo.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    // Handle ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "RESOURCE_NOT_FOUND");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle TokenRefreshException
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(TokenRefreshException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "UNAUTHORIZED");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
 // Handle BadRequestException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "BAD_REQUEST");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle UnauthorizedException
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "UNAUTHORIZED");
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // Handle InternalServerErrorException
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "INTERNAL_SERVER_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    // Handle MethodArgumentNotValidException (Validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(String.valueOf(ex));
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMessage.append(fieldError.getField())
                    .append(": ")
                    .append(fieldError.getDefaultMessage())
                    .append("; ");
        }
        ErrorResponse errorResponse = new ErrorResponse(errorMessage.toString(), "VALIDATION_FAILED");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    // Handle ConstraintViolationException (JPA validation errors)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(String.valueOf(ex));
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errorMessage.append(violation.getPropertyPath())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
        }
        ErrorResponse errorResponse = new ErrorResponse(errorMessage.toString(), "CONSTRAINT_VIOLATION");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), "INVALID_ARGUMENT");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    // Handle HttpMessageNotReadableException (Invalid JSON)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse("Invalid JSON format", "BAD_REQUEST");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    
    // Handle MethodArgumentTypeMismatchException (Type mismatch)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error(String.valueOf(ex));
        String errorMessage = "Invalid argument type: " + ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ErrorResponse errorResponse = new ErrorResponse(errorMessage, "TYPE_MISMATCH");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    // Handle DataIntegrityViolationException (Database constraint violation)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse("Database constraint violation", "DATABASE_ERROR");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    // Handle AccessDeniedException (Permission denied)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse("Access denied", "ACCESS_DENIED");
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
    
    // Handle HttpRequestMethodNotSupportedException (Invalid HTTP method)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error(String.valueOf(ex));
        ErrorResponse errorResponse = new ErrorResponse("HTTP method not supported", "METHOD_NOT_SUPPORTED");
        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
    }
    
    
    // For browser pages
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFound(NoHandlerFoundException ex, HttpServletRequest request) {
        log.error(String.valueOf(ex));
        String accept = request.getHeader("Accept");
        
        // If client wants JSON (API), send JSON
        if (accept != null && accept.contains("application/json")) {
            ErrorResponse errorResponse = new ErrorResponse("No handler found for " + request.getRequestURI(), "ROUTE_NOT_FOUND");
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
        
        // Else, return 404.jsp
        return "404";
    }
    
    @ExceptionHandler(Exception.class)
    public Object handleGeneric(Exception ex, HttpServletRequest request) {
        log.error(String.valueOf(ex));
        String accept = request.getHeader("Accept");
        
        if (accept != null && accept.contains("application/json")) {
            ErrorResponse errorResponse = new ErrorResponse("An unexpected error occurred", "INTERNAL_SERVER_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
        
        return "error"; // return to error.jsp page
    }
}
