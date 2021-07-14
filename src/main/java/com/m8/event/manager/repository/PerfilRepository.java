
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Perfil;
import java.util.Date;
import java.util.List;

import com.m8.event.manager.entity.Usuario;
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
    
    @Query("SELECT per FROM Perfil per WHERE per.usuario.rol.nombreRol=:rol")
    public List<Perfil> verListaDeProfesores (@Param("rol") String nombreRol);
    
    public List<Perfil> findByFechaNac (Date fechaNac);
    
    public Perfil findByEmail (String email);   
    
    public Perfil findByUsuario(Usuario usuario);
    
}
