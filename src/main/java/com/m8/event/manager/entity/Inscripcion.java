package com.m8.event.manager.entity;

import com.m8.event.manager.enumeration.Modalidad;
import com.m8.event.manager.enumeration.Estado;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Inscripcion implements Serializable {

   @Id
   @GeneratedValue (strategy= GenerationType.IDENTITY)
   private Integer idInscripcion;

   @ManyToOne
   private Evento evento;

   @ManyToOne
   private Usuario alumno;

   @Enumerated(EnumType.STRING)
   private Estado estado;

   @Enumerated(EnumType.STRING)
   private Modalidad modalidad;

}
