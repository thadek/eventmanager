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

        validarDatos(idEvento, emailAlumno, modalidad);

        Optional<Evento> respuesta1 = eventoRepository.findById(idEvento);
         Perfil alumno = perfilRepository.findByEmail(emailAlumno);

      //  System.out.println("Inscripcion: ID EVENTO:"+idEvento+" EmailAlumno:"+emailAlumno+" Modalidad: "+modalidad);

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

        String subject = "Nueva Inscripci??n";
        String text;
        if (inscripcion.getEstado().equals(Estado.PENDIENTE)) {
            text = "Hola " + alumno.getNombre() + "! \n Ten??s un lugar reservado "
                    + " en el evento " + evento.getNombre() + ". Tu inscripci??n se encuentra "
                    + "PENDIENTE DE CONFIRMACI??N. Para confirmarla, por favor envianos "
                    + "el comprobante de pago.";
        } else {
            text = "Hola " + alumno.getNombre() + "! \n El evento " + evento.getNombre()
                    + " en el cual te inscribiste tiene el cupo completo. Sin embargo, "
                    + "quedaste inscripto en LISTA DE ESPERA. En caso de que se liberen "
                    + "lugares, te avisaremos inmediatamente por email.";
        }

        emailService.enviarCorreo(alumno.getEmail(), subject, text);

    }

    @Transactional
    public void modificarInscripcion(Integer idInscripcion, Integer idEvento,
            String emailAlumno, Modalidad modalidad, Estado estado) throws ErrorServicio, MessagingException {

        String subject = "";
        String text = "";

        validarDatos(idEvento, emailAlumno, modalidad);

        Optional<Inscripcion> respuesta = inscripcionRepository.findById(idInscripcion);
        Optional<Evento> respuesta1 = eventoRepository.findById(idEvento);
        Perfil alumno = perfilRepository.findByEmail(emailAlumno);

        if (!respuesta.isPresent()) {
            throw new ErrorServicio("No existe esa inscripci??n en la base de datos.");
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

        //El alumno podr?? cambiar la modalidad siempre que haya cupo en la otra modalidad.
        if (evento.getModalidad().equals(Modalidad.MIXTA)) {
            if (!inscripcion.getModalidad().equals(modalidad)) {
                int cantidadInscripciones = inscripcionRepository.cantidadInscripciones(idEvento, modalidad, Arrays.asList(Estado.PENDIENTE, Estado.CONFIRMADO));
                if (modalidad.equals(Modalidad.PRESENCIAL)) {
                    if (cantidadInscripciones < evento.getCupoPresencial()) {
                        inscripcion.setModalidad(modalidad);

                        subject = "Cambiaste la modalidad de tu inscripci??n";
                        text = "Hola " + alumno.getNombre() + "! \n Tu inscripci??n "
                                + "en el evento " + evento.getNombre() + " cambi?? "
                                + "a modalidad PRESENCIAL.";

                    } else {
                        throw new ErrorServicio("No hay cupo disponible en la modalidad "
                                + modalidad.toString().toLowerCase() + ".");
                    }
                }
                if (modalidad.equals(Modalidad.ONLINE)) {
                    if (cantidadInscripciones < evento.getCupoVirtual()) {
                        inscripcion.setModalidad(modalidad);

                        subject = "Cambiaste la modalidad de tu inscripci??n";
                        text = "Hola " + alumno.getNombre() + "! \n Tu inscripci??n "
                                + "en el evento " + evento.getNombre() + " cambi?? "
                                + "a modalidad ONLINE.";

                    } else {
                        throw new ErrorServicio("No hay cupo disponible en la modalidad "
                                + modalidad.toString().toLowerCase() + ".");
                    }
                }
            }
        }
        // Los administradores pueden cambiar a cualquier estado. Alumno s??lo puede 
        //cambiar a Estado CANCELADO.
        inscripcion.setEstado(estado);

        inscripcionRepository.save(inscripcion);
        
        if (estado.equals(Estado.CONFIRMADO)) {

            subject = "Tu incripci??n fue confirmada";
            text = "Hola " + alumno.getNombre() + "! \n Tu inscripci??n en el evento "
                    + evento.getNombre() + " fue confirmada. Te esperamos el d??a " 
                    + evento.getFechaInicio() + " a las " + evento.getHora() + ". "
            + "??Nos encontramos ah??! :)";            

        }

        if (estado.equals(Estado.CANCELADO)) {

            subject = "Tu incripci??n fue cancelada";
            text = "Hola " + alumno.getNombre() + "! \n Tu inscripci??n en el evento "
                    + evento.getNombre() + " fue cancelada. En caso de que hayas "
                    + "pagado, por favor comunicate con nosotros.";

            chequearListaDeEspera(idEvento, modalidad);

        }

        emailService.enviarCorreo(alumno.getEmail(), subject, text);

    }

    public void chequearListaDeEspera(Integer idEvento, Modalidad modalidad) throws MessagingException {

        Optional<Inscripcion> respuesta = inscripcionRepository.buscarListaDeEspera(idEvento, modalidad);

        if (!respuesta.isPresent()) {
            return;
        }
        Inscripcion inscripcion = respuesta.get();

        inscripcion.setEstado(Estado.PENDIENTE);
        inscripcionRepository.save(inscripcion);

        String subject = "Ten??s un lugar en el evento!";
        String text = "Hola " + inscripcion.getAlumno().getNombre() + "! \n "
                + "Se liber?? un lugar en el evento " + inscripcion.getEvento().getNombre() 
                + ". En caso de que todav??a quieras participar, te pedimos que nos "
                + "confirmes tu inscripci??n, dentro de las pr??ximas 24 horas, "
                + "envi??ndonos el comprobante de pago. En caso de que no puedas "
                + "o quieras participar, te pedimos que canceles tu inscripci??n "
                + "lo antes posible, para que otra persona pueda ocupar tu lugar.";

        emailService.enviarCorreo(inscripcion.getAlumno().getEmail(), subject, text);

    }

    public List<Inscripcion> inscripcionesPorAlumno(String email) throws ErrorServicio {

        return inscripcionRepository.inscripcionesPorAlumno(email);
    }
    
    public List<Inscripcion> inscripcionesPendientes() throws ErrorServicio {

        return inscripcionRepository.inscripcionesPendientes();
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
            throw new ErrorServicio("No se asign?? ning??n alumno al evento.");
        }
    }

}
