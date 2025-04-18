package com.example.persona.demo.Exceptions;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Conditional;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer code;
    private String status;
    private LocalDateTime timestamp;
    private String message;
    private String path;
}
