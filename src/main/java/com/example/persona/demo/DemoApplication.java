package com.example.persona.demo;

import com.example.persona.demo.Configuration.Datasource.DataSourceContextHolder;
import com.example.persona.demo.Entity.db1.Persona;
import com.example.persona.demo.Entity.db2.Producto;
import com.example.persona.demo.Mapper.PersonaMapper;
import com.example.persona.demo.Repository.PersonaRepository;
import com.example.persona.demo.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;
import java.time.LocalDate;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.persona.demo")
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void run(String... args) throws Exception {
		//POR DEFECTO LA DB1 ES QUIEN ESTA ACTIVA EN EL CONTEXTO
		PersonaRepository personaRepository = this.applicationContext.getBean(PersonaRepository.class);
		PersonaMapper personaMapper = this.applicationContext.getBean(PersonaMapper.class);
		Persona persona = Persona.builder()
				.name("master")
				.lastname("master")
				.dni("43463078")
				.email("bautistabasiliodev@outlook.com")
				.birthDate(LocalDate.of(2001, 7,17))
				.build();
		personaRepository.save(persona);
		personaRepository.findAll().forEach(e->System.out.println(personaMapper.toPersonaDTO(e)));
		System.out.println(personaRepository.existsByDniOrEmail(persona.getDni(),persona.getEmail()));

		//SETEAMOS EL CONTEXTO REEMPLAZANDO LA DB1 POR LA DB2 EN EL MISMO HILO
		DataSourceContextHolder.setDataSource("db2");
		ProductoRepository productoRepository = this.applicationContext.getBean(ProductoRepository.class);
		Producto producto = Producto.builder()
				.name("Televisor")
				.build();
		productoRepository.save(producto);
		System.out.println(productoRepository.findAll());

		DataSourceContextHolder.clearDataSource();
	}
}
