package com.example.persona.demo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaRequestDTO {

    @NotNull
    @Size(max = 50,min = 2, message = "Name must be between 2 and 50 characters")
    private String name;

    @Size(max = 50,min = 2, message = "Lastname must be between 2 and 50 characters")
    private String lastname;

    @Size(min = 8, message = "Dni must have at least 8 digits")
    private String dni;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Birthdate is required")
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDate;
}
