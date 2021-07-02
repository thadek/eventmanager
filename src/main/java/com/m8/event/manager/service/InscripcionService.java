package com.m8.event.manager.service;

import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.enumeration.Estado;
import com.m8.event.manager.enumeration.Modalidad;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.InscripcionRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscripcionService {
    
    @Autowired
    InscripcionRepository inscripcionRepository;
    
    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    
    
    @Transactional
    public void crearInscripcion(Integer idEvento, String username, Modalidad modalidad) 
            throws ErrorServicio {
        
        validarDatos(idEvento, username, modalidad);
        
        Optional<Evento> respuesta1 = eventoRepository.findById(idEvento);
        Optional<Usuario> respuesta2 = usuarioRepository.findById(username);
        
        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe ese evento en la base de datos.");
        }
        if (!respuesta2.isPresent()) {
            throw new ErrorServicio("No existe ese alumno en la base de datos.");
        }
        
        Inscripcion inscripcion = new Inscripcion();
        Evento evento = respuesta1.get();
        Usuario alumno = respuesta2.get();        
        
        inscripcion.setEvento(evento);
        inscripcion.setAlumno(alumno);
        inscripcion.setModalidad(modalidad);
        
        List<Inscripcion> inscripciones = evento.getInscripciones();
        int inscripcionesPresenciales = 0;
        int inscripcionesOnline = 0;
                
        switch (modalidad) {
            case PRESENCIAL:
                     
                for (Inscripcion inscrip : inscripciones) {
                    if(inscrip.getModalidad().equals(modalidad.PRESENCIAL)){
                        inscripcionesPresenciales ++;
                    }
                }               
                
                if (inscripcionesPresenciales < evento.getCupoPresencial()) {
                    inscripcion.setEstado(Estado.PENDIENTE);
                } else {
                    inscripcion.setEstado(Estado.ESPERA);
                }
                break;
            case ONLINE:
                
                for (Inscripcion inscrip : inscripciones) {
                    if(inscrip.getModalidad().equals(modalidad.ONLINE)){
                        inscripcionesOnline ++;
                    }
                }               
                
                if (inscripcionesOnline < evento.getCupoVirtual()) {
                    inscripcion.setEstado(Estado.PENDIENTE);
                } else {
                    inscripcion.setEstado(Estado.ESPERA);
                }
                break;                       
        }
        
        inscripcionRepository.save(inscripcion);
                        
    }   
    
    public void validarDatos(Integer idEvento, String alumno, Modalidad modalidad) 
            throws ErrorServicio {

        if (idEvento == null) {
            throw new ErrorServicio("Debe elegir un evento.");
        }

        if (modalidad == null) {
            throw new ErrorServicio("Debe indicar la modalidad al evento.");
        }

        if (alumno == null || alumno.isEmpty()) {
            throw new ErrorServicio("No se asignó ningún alumno al evento.");
        }
    }
    
}
