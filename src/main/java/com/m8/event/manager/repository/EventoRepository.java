
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.enumeration.Modalidad;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EventoRepository extends JpaRepository<Evento,Integer> {
    
    
    @Query("SELECT e FROM Evento e WHERE e.nombre LIKE :%nombre%")
    public List<Evento> buscarPorNombre (@Param("%nombre%") String nombre);
    
    //Ver sintaxis con Cocco
    @Query("SELECT e FROM Evento e WHERE e.subcategoria.idSubcategoria= :idSubcategoria")
    public List<Evento> buscarPorSubcategoria (@Param("idSubcategoria") Integer idSubcategoria);
    
    //Ver sintaxis con Cocco
    @Query("SELECT e FROM Evento e WHERE e.usuario.username= :username")
    public List<Evento> buscarPorFacilitador (@Param("username") String username);
    
    @Query("SELECT e FROM Evento e WHERE e.modalidad= :modalidad")
    public List<Evento> buscarPorModalidad (@Param("modalidad") Modalidad modalidad);
        
//     List<EventoRepository> findByFecha (Date fecha); 
  
    public List<Evento> findByFechaInicio (LocalDate fechaInicio);  
    
}
