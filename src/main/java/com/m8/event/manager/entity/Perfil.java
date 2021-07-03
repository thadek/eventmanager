package com.m8.event.manager.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class Perfil implements Serializable {

   @Id
   @GeneratedValue (strategy= GenerationType.IDENTITY)
   private Integer idPerfil;

   @Column (unique = true)
   private String email;

   private String nombre;

   private String apellido;

   private String tel;

   private LocalDate fechaNac;

   private String descripcion;

   @OneToOne
   private Usuario usuario;
   

   private String fotoURL;

}
