package com.m8.event.manager.entity;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

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

   private MultipartFile foto;
//   @Lob
//   @Column(name="imagen")
//   Private byte[] imagen;




}
