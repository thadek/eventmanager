package com.m8.event.manager.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Data
public class Usuario implements Serializable {

   @Id
   private String username;

   private String password;

   @ManyToOne
   private Rol rol;

}
