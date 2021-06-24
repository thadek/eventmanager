
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Evento;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EventoRepository extends JpaRepository<Evento,Integer> {
    
    
    @Query("SELECT e FROM Evento e WHERE e.nombre= :nombre")
    public List<Evento> buscarPorEvento (@Param("nombre") String nombre);
    
    @Query("SELECT e FROM Evento e WHERE e.modalidad= :modalidad")
    public List<Evento> buscarPorModalidad (@Param("modalidad") String modalidad);
    
//    List<EventoRepository> findByFecha (Date fecha); VER QUE DEVUELVE...
    public List<Evento> findByFecha (Date fecha);
    
}
