
package com.m8.event.manager.repositorios;

import com.m8.event.manager.entity.Perfil;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer>{
    
    @Query("SELECT per FROM Perfil per WHERE per.nombre=:nombre")
    public List<Perfil> buscarPorNombre (@Param("nombre") String nombre);
    
    @Query("SELECT per FROM Perfil per WHERE per.apellido=:apellido")
    public List<Perfil> buscarPorApellido (@Param("apellido") String apellido);
    
    public List<Perfil> findByFechaNac (Date fechaNac);
    
    public List<Perfil> findByEmail (String email);
    
    
    
}
