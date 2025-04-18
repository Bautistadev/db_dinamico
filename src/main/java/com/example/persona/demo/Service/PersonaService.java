package com.example.persona.demo.Service;

import com.example.persona.demo.DTO.PersonaRequestDTO;
import com.example.persona.demo.Exceptions.Custom.BadRequestException;
import com.example.persona.demo.Mapper.PersonaMapper;
import com.example.persona.demo.Repository.PersonaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    private PersonaRepository personaRepository;
    private PersonaMapper personaMapper;

    public PersonaService(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }

    @Transactional
    public void save(PersonaRequestDTO personaRequestDTO){
        if(this.personaRepository.existsByDniOrEmail(personaRequestDTO.getDni(),personaRequestDTO.getEmail()))
            throw new BadRequestException("Persona existente");
        this.personaRepository.save(personaMapper.toPersona(personaRequestDTO));

    }




}
