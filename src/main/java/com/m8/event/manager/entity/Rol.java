package com.m8.event.manager.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Rol implements Serializable {

   @Id
   private Integer idRol;

   @Column (unique = true)
   private String nombreRol;

}
