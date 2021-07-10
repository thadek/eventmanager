package com.m8.event.manager.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Categoria implements Serializable {

   @Id
   @GeneratedValue (strategy= GenerationType.IDENTITY)
   private Integer idCategoria;

   @Column(unique = true)
   private String nombre;

   @JsonManagedReference
   @OneToMany (mappedBy = "categoria")
   private List<Subcategoria> subcategorias;

}
