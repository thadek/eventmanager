package com.m8.event.manager.service;

import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.enumeration.Estado;
import com.m8.event.manager.enumeration.Modalidad;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.InscripcionRepository;
import com.m8.event.manager.repository.PerfilRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
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

    @Autowired
    PerfilRepository perfilRepository;

    @Autowired
    EmailService emailService;

    @Transactional
    public void crearInscripcion(Integer idEvento, String emailAlumno, Modalidad modalidad)
            throws ErrorServicio, MessagingException {

       // validarDatos(idEvento, emailAlumno, modalidad);

        Optional<Evento> respuesta1 = eventoRepository.findById(idEvento);
         Perfil alumno = perfilRepository.findByEmail(emailAlumno);





        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe ese evento en la base de datos.");
        }
        if (alumno == null) {
            throw new ErrorServicio("No existe ese alumno en la base de datos.");
        }

        Inscripcion inscripcion = new Inscripcion();
        Evento evento = respuesta1.get();


        inscripcion.setEvento(evento);
        inscripcion.setAlumno(alumno);
        inscripcion.setModalidad(modalidad);

        List<Inscripcion> inscripciones = evento.getInscripciones();
        inscripciones.sort((Inscripcion i1, Inscripcion i2)
                -> Integer.compare(i1.getIdInscripcion(), i2.getIdInscripcion()));

        int cantidadInscripciones = inscripcionRepository.cantidadInscripciones(idEvento, modalidad, Arrays.asList(Estado.PENDIENTE, Estado.CONFIRMADO));
//        int inscripcionesOnline = inscripcionRepository.cantidadInscripciones(modalidad, Arrays.asList(Estado.PENDIENTE, Estado.CONFIRMADO));;




        inscripcion.setEstado(cantidadInscripciones < evento.getCupoPresencial() ? Estado.PENDIENTE : Estado.ESPERA);
        if (modalidad.equals(Modalidad.ONLINE)) {
            inscripcion.setEstado(cantidadInscripciones < evento.getCupoVirtual() ? Estado.PENDIENTE : Estado.ESPERA);
        }

        if (modalidad.equals(Modalidad.PRESENCIAL)) {
            if (cantidadInscripciones < evento.getCupoPresencial()) {
                inscripcion.setEstado(Estado.PENDIENTE);
            } else {
                inscripcion.setEstado(Estado.ESPERA);
            }
        } else {
            if (cantidadInscripciones < evento.getCupoVirtual()) {
                inscripcion.setEstado(Estado.PENDIENTE);
            } else {
                inscripcion.setEstado(Estado.ESPERA);
            }
        }


        inscripcionRepository.save(inscripcion);

        String subject = "Nueva Inscripción";
        String text;
        if (inscripcion.getEstado().equals(Estado.PENDIENTE)) {
            text = "Estimad@ " + alumno.getNombre() + ": \n Tenés un lugar reservado "
                    + " en el evento " + evento.getNombre() + ", que se va se encuentra en . "
                    + "Para confirmarla, por favor comunicate ";
        } else {
            text = "Estimad@ " + alumno.getNombre() + ": \n Tu inscripción "
                    + "el evento " + evento.getNombre() + "en el cual sos el facilitador.";
        }

        emailService.enviarCorreo(alumno.getEmail(), subject, text);

        //        switch (modalidad) {
//            case PRESENCIAL:
//                inscripcionesPresenciales = (int) inscripciones.stream()
//                        .filter(i -> Modalidad.PRESENCIAL.equals(i.getModalidad()))
//                        .count();               
//                for (Inscripcion inscrip : inscripciones) {
//
//                    if (inscrip.getModalidad().equals(modalidad)) {
//                        if (inscrip.getEstado().equals(Estado.PENDIENTE)
//                                || inscrip.getEstado().equals(Estado.CONFIRMADO)) {
//                            inscripcionesPresenciales++;
//                        }
//                    }
//                }    
////            case ONLINE:
//        for (Inscripcion inscrip : inscripciones) {
//            if (inscrip.getModalidad().equals(modalidad)) {
//                if (inscrip.getEstado().equals(Estado.PENDIENTE)
//                        || inscrip.getEstado().equals(Estado.CONFIRMADO)) {
//                    inscripcionesOnline++;
//                }
//            }
//        }       
    }

    @Transactional
    public void modificarInscripcion(Integer idInscripcion, Integer idEvento,
            String emailAlumno, Modalidad modalidad, Estado estado) throws ErrorServicio {

        validarDatos(idEvento, emailAlumno, modalidad);

        Optional<Inscripcion> respuesta = inscripcionRepository.findById(idInscripcion);
        Optional<Evento> respuesta1 = eventoRepository.findById(idEvento);
        Perfil alumno = perfilRepository.findByEmail(emailAlumno);

        if (!respuesta.isPresent()) {
            throw new ErrorServicio("No existe esa inscripción en la base de datos.");
        }
        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe ese evento en la base de datos.");
        }
        if (alumno == null) {
            throw new ErrorServicio("No existe ese alumno en la base de datos.");
        }

        Inscripcion inscripcion = respuesta.get();
        Evento evento = respuesta1.get();

        inscripcion.setEvento(evento);
        inscripcion.setAlumno(alumno);

        if (evento.getModalidad().equals(Modalidad.MIXTA)) {
            if (!inscripcion.getModalidad().equals(modalidad)) {
                int cantidadInscripciones = inscripcionRepository.cantidadInscripciones(idEvento, modalidad, Arrays.asList(Estado.PENDIENTE, Estado.CONFIRMADO));
                if (modalidad.equals(Modalidad.PRESENCIAL)) {
                    if (cantidadInscripciones < evento.getCupoPresencial()) {
                        inscripcion.setModalidad(modalidad);
                    } else {
                        throw new ErrorServicio("No hay cupo disponible en la modalidad "
                                + modalidad.toString().toLowerCase() + ".");
                    }
                }
                if (modalidad.equals(Modalidad.ONLINE)) {
                    if (cantidadInscripciones < evento.getCupoVirtual()) {
                        inscripcion.setModalidad(modalidad);
                    } else {
                        throw new ErrorServicio("No hay cupo disponible en la modalidad "
                                + modalidad.toString().toLowerCase() + ".");
                    }
                }
            }
        }
        // Los administradores pueden cambiar a cualquier estado. Alumno sólo puede 
        //cambiar a Estado CANCELADO.
        inscripcion.setEstado(estado);

        if (estado.equals(Estado.CANCELADO)) {
            chequearListaDeEspera(idEvento, modalidad);
        }

        inscripcionRepository.save(inscripcion);

    }

    public void chequearListaDeEspera(Integer idEvento, Modalidad modalidad) {

//        List<Inscripcion> inscripciones = evento.getInscripciones();
//        inscripciones.sort((Inscripcion i1, Inscripcion i2)
//                -> Integer.compare(i1.getIdInscripcion(), i2.getIdInscripcion()));
        Optional<Inscripcion> respuesta = inscripcionRepository.buscarListaDeEspera(idEvento, modalidad);

        if (!respuesta.isPresent()) {
            return;
        }
        Inscripcion inscripcion = respuesta.get();

        inscripcion.setEstado(Estado.PENDIENTE);
        inscripcionRepository.save(inscripcion);

//        for (Inscripcion inscrip : inscripciones) {
//
//            if (inscrip.getModalidad().equals(modalidad)
//                    && inscrip.getEstado().equals(Estado.ESPERA)) {
//                inscrip.setEstado(Estado.PENDIENTE);
//                inscripcionRepository.save(inscrip);
//                break;
//            }
//        }
    }
    
    public List<Inscripcion> inscripcionesPorAlumno(String email) throws ErrorServicio {
        
        return inscripcionRepository.inscripcionesPorAlumno(email);
    }

    public void validarDatos(Integer idEvento, String emailAlumno, Modalidad modalidad)
            throws ErrorServicio {

        if (idEvento == null) {
            throw new ErrorServicio("Debe elegir un evento.");
        }

        if (modalidad == null) {
            throw new ErrorServicio("Debe indicar la modalidad al evento.");
        }

        if (emailAlumno == null || emailAlumno.isEmpty()) {
            throw new ErrorServicio("No se asignó ningún alumno al evento.");
        }
    }

}
