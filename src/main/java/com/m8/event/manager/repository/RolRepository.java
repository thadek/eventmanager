
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.entity.Rol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    @Query("SELECT r FROM Rol r WHERE r.nombreRol= :nombreRol")
    public Rol buscarPorRol (@Param("nombreRol")String nombreRol);

//    public Rol findByRol (String nombreRol);
}
