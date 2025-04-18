package com.example.persona.demo.Service;

import com.example.persona.demo.DTO.PersonaDTO;
import com.example.persona.demo.DTO.PersonaRequestDTO;
import com.example.persona.demo.Exceptions.Custom.BadRequestException;
import com.example.persona.demo.Mapper.PersonaMapper;
import com.example.persona.demo.Notations.SelectDB;
import com.example.persona.demo.Repository.PersonaRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaService {

    private PersonaRepository personaRepository;
    private PersonaMapper personaMapper;

    public PersonaService(PersonaRepository personaRepository, PersonaMapper personaMapper) {
        this.personaRepository = personaRepository;
        this.personaMapper = personaMapper;
    }


    @Transactional
    @SelectDB(value = "db1")
    public void save(PersonaRequestDTO personaRequestDTO){
        if(this.personaRepository.existsByDniOrEmail(personaRequestDTO.getDni(),personaRequestDTO.getEmail()))
            throw new BadRequestException("Persona existente");
        this.personaRepository.save(personaMapper.toPersona(personaRequestDTO));

    }

    @SelectDB(value = "db1")
    public List<PersonaDTO> findAll(){
        return this.personaRepository.findAll()
                .stream()
                .map(e->this.personaMapper.toPersonaDTO(e)).collect(Collectors.toList());
    }




}
