package com.m8.event.manager.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

/** @author Agustin */


@Entity
@Data
public class Foto {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    private String nombre;
    private String mime;
    
    @OneToOne
    private Perfil perfil;
    
    
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte [] contenido;

    

    

    
    

    
    
            
    
    
    
}
