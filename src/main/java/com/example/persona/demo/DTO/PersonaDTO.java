package com.example.persona.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonaDTO {

    private Long id;

    private String name;

    private String lastname;

    private String dni;

    private String email;

    private LocalDate birthDate;
}
