
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String>{
    
    List<Usuario> findByUsuario (String username);
    
}
