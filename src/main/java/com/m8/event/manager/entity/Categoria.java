package com.m8.event.manager.entity;

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

   @OneToMany (mappedBy = "categoria")
   private List<Subcategoria> subcategorias;

}
