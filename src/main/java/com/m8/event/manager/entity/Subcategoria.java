package com.m8.event.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

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

   @ToString.Exclude
   @ManyToOne
   private Categoria categoria;

   private String descripcion;
   
}
