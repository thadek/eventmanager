package com.m8.event.manager.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
public class Rol implements Serializable {

   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   private Integer idRol;

   @Column (unique = true)
   private String nombreRol;

}
