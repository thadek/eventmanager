package com.m8.event.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.enumeration.Modalidad;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class Evento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @ManyToOne
    private Subcategoria subcategoria;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    private LocalDate fechaInicio; //sacamos temporal y agregamos anotación en main

    private LocalDate fechaFin;

    @ElementCollection(targetClass = Dia.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    private List<Dia> dias;

    private LocalTime hora;

    private Integer duracion;

    private Integer cupoPresencial;

    private Integer cupoVirtual;

    private Integer valor;

    @JsonManagedReference
    @OneToMany(mappedBy = "evento")
    private List<Inscripcion> inscripciones;

    @ManyToOne
    private Perfil facilitador;

    private String descripcion;

}
