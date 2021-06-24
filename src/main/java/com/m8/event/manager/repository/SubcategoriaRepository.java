
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.entity.Subcategoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Integer> {
    
       
    @Query("SELECT s FROM Subcategoria s WHERE s.nombre= :nombre")
    public List<Subcategoria> buscarPorNombre (@Param("nombre") String nombre);
    
}
