package com.three_tech_solutions.slot_app.exceptions;

import com.three_tech_solutions.slot_app.exceptions.responses.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException responseStatusException, HttpServletRequest request) {
        return ResponseEntity
                .status(responseStatusException.getStatusCode())
                .body(ApiError.builder()
                        .status(responseStatusException.getStatusCode().value())
                        .errors(List.of(responseStatusException.getReason()))
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
                );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .errors(errores)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build()
                );
    }

}

