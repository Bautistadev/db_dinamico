package com.example.persona.demo.Entity.db1;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.LocalDate;

@Entity
@Table(name = "Personas")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@SQLDelete(sql="UPDATE Personas SET delete_date = current_timestamp WHERE id = ?")
@SQLRestriction("delete_date IS NULL")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "lastname",nullable = false)
    private String lastname;

    @Column(name = "dni",nullable = false,unique = true,length = 12)
    private String dni;

    @Column(name= "email", nullable = false)
    private String email;

    @Column(name= "birthdate", nullable = false)
    private LocalDate birthDate;

    @Column(updatable = false,name = "create_date")
    @CreationTimestamp
    private LocalDate createDate;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name="delete_date")
    private LocalDate deleteDate;

}
