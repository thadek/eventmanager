
package com.m8.event.manager.repository;

import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.enumeration.Estado;
import com.m8.event.manager.enumeration.Modalidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    
    @Query ("SELECT Count(i) FROM Inscripcion i WHERE i.evento.idEvento= :idEvento "
            + "AND i.modalidad= :modalidad AND i.estado IN :estados")
    public int cantidadInscripciones (@Param("idEvento") Integer idEvento,
            @Param("modalidad") Modalidad modalidad, 
            @Param("estados") List<Estado> estados);
    
    @Query ("SELECT i FROM Inscripcion i WHERE i.evento.idEvento= :idEvento "
            + "AND i.modalidad= :modalidad AND i.estado = :estado "
            + "ORDER BY i.idInscripcion ASC "
            + "LIMIT 1")
    public Inscripcion buscarListaDeEspera (@Param("idEvento") Integer idEvento,
            @Param("modalidad") Modalidad modalidad, 
            @Param("estado") Estado estado);    
}