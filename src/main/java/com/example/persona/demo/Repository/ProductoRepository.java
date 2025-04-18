package com.example.persona.demo.Repository;

import com.example.persona.demo.Entity.db2.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto,Long> {
}
