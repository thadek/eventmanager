
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.entity.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
}
