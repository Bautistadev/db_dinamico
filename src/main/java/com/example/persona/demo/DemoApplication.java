package com.example.persona.demo;

import com.example.persona.demo.DTO.PersonaRequestDTO;
import com.example.persona.demo.Service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


import java.time.LocalDate;

@SpringBootApplication
@EnableAspectJAutoProxy
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
		PersonaService personaRepository = this.applicationContext.getBean(PersonaService.class);
		PersonaRequestDTO persona = PersonaRequestDTO.builder()
				.name("master")
				.lastname("master")
				.dni("43463078")
				.email("bautistabasiliodev@outlook.com")
				.birthDate(LocalDate.of(2001, 7,17))
				.build();
		personaRepository.save(persona);
		System.out.println(personaRepository.findAll());

		//SETEAMOS EL CONTEXTO REEMPLAZANDO LA DB1 POR LA DB2 EN EL MISMO HILO
		/*DataSourceContextHolder.setDataSource("db2");
		ProductoRepository productoRepository = this.applicationContext.getBean(ProductoRepository.class);
		Producto producto = Producto.builder()
				.name("Televisor")
				.build();
		productoRepository.save(producto);
		System.out.println(productoRepository.findAll());

		DataSourceContextHolder.clearDataSource();*/
	}
}
