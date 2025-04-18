package com.example.persona.demo.Repository;

import com.example.persona.demo.Entity.db1.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonaRepository extends JpaRepository<Persona,Long> {
    public Boolean existsByDniOrEmail(String dni,String email);
}
