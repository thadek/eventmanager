package com.m8.event.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Subcategoria implements Serializable {

   @Id
   @GeneratedValue (strategy = GenerationType.IDENTITY)
   private Integer idSubcategoria;

   private String nombre;

   @JsonBackReference
   @ManyToOne
   private Categoria categoria;

   private String descripcion;
   
}
