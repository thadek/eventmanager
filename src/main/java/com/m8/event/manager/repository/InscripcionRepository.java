
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.enumeration.Estado;
import com.m8.event.manager.enumeration.Modalidad;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
    @Query ("SELECT Count(i) FROM Inscripcion i WHERE i.evento.id= :idEvento "
            + "AND i.modalidad= :modalidad AND i.estado IN :estados")
    public int cantidadInscripciones (@Param("idEvento") Integer idEvento,
            @Param("modalidad") Modalidad modalidad, 
            @Param("estados") List<Estado> estados);

    @Query (value= "SELECT * FROM Inscripcion i WHERE i.evento_id= :idEvento "
            + "AND i.modalidad= :modalidad AND i.estado = 'ESPERA' "
            + "ORDER BY i.id_inscripcion ASC "
            + "LIMIT 1", nativeQuery = true)
    public Optional<Inscripcion> buscarListaDeEspera (@Param("idEvento") Integer idEvento,
            @Param("modalidad") Modalidad modalidad);
    
    @Query ("SELECT i FROM Inscripcion i WHERE i.estado = 'PENDIENTE'")
    public List<Inscripcion> inscripcionesPendientes();
    
    @Query ("SELECT i FROM Inscripcion i WHERE i.alumno.usuario.username= :username")
    public List<Inscripcion> inscripcionesPorAlumno (@Param("username") String username);
    
    @Query ("SELECT i FROM Inscripcion i WHERE i.evento.id= :idEvento "
            + "AND i.estado= 'CONFIRMADO'")
    public List<Inscripcion> InscripcionesConfirmadas (@Param("idEvento") Integer idEvento);
        
}
