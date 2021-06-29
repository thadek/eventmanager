/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.entity.Foto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FotoRepository extends JpaRepository<Foto, String> {
    
   public List<Foto> findByNombre (String nombre);
    
}
