
package com.m8.event.manager.repositorios;

import com.m8.event.manager.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    
    @Query("SELECT c FROM Categoria c WHERE c.nombre= :nombre")
    public void buscarPorCategoria (@Param("nombre")String nombre);
    
}
