package com.example.persona.demo.Mapper;

import com.example.persona.demo.DTO.PersonaDTO;
import com.example.persona.demo.DTO.PersonaRequestDTO;
import com.example.persona.demo.Entity.db1.Persona;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PersonaMapper {

    public abstract PersonaDTO toPersonaDTO(Persona persona);
    public abstract Persona toPersona(PersonaRequestDTO personaRequestDTO);
    public abstract Persona toPersona(PersonaDTO personaDTO);
}
