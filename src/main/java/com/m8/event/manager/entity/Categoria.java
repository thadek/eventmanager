package com.m8.event.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.m8.event.manager.rest.serializer.SubcategoriaListDeserializer;
import com.m8.event.manager.rest.serializer.SubcategoriaListSerializer;
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


   @JsonDeserialize(using = SubcategoriaListDeserializer.class)
   @JsonSerialize(using= SubcategoriaListSerializer.class)
   @OneToMany (mappedBy = "categoria", cascade = {CascadeType.ALL})
   private List<Subcategoria> subcategorias;

}
