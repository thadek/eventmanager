package com.m8.event.manager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import javax.persistence.OneToOne;

@Entity
@Data
public class Usuario implements Serializable {

   @Id
   private String username;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String password;

   @ManyToOne
   private Rol rol;
   
}
