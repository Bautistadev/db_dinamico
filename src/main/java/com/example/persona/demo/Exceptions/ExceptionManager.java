package com.example.persona.demo.Exceptions;

import com.example.persona.demo.Exceptions.Custom.BadRequestException;
import com.example.persona.demo.Exceptions.Custom.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(BadRequestException badRequestException, HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message(badRequestException.getMessage())
                .path(httpServletRequest.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException notFoundException,HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message(notFoundException.getMessage())
                .path(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> internalServerError(Exception exception, HttpServletRequest httpServletRequest){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .timestamp(LocalDateTime.now())
                .message("Internal server error")
                .path(httpServletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
