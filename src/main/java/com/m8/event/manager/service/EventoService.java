package com.m8.event.manager.service;

import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.entity.Subcategoria;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.enumeration.Estado;
import com.m8.event.manager.enumeration.Modalidad;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.InscripcionRepository;
import com.m8.event.manager.repository.PerfilRepository;
import com.m8.event.manager.repository.SubcategoriaRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    @Autowired
    EventoRepository eventoRepository;

    @Autowired
    SubcategoriaRepository subcategoriaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PerfilRepository perfilRepository;

    @Autowired
    EmailService emailService;
    
    @Autowired
    InscripcionRepository inscripcionRepository;

    @Transactional
    public void crearEvento(String nombre, Integer idSubcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            List<Dia> dias, LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, String emailFacilitador,
            String descripcion) throws ErrorServicio, MessagingException {

        boolean crear = true;

        validarDatos(crear, nombre, idSubcategoria, modalidad, fechaInicio, fechaFin,
                hora, duracion, cupoPresencial, cupoVirtual, valor, emailFacilitador,
                descripcion);

        Optional<Subcategoria> respuesta1 = subcategoriaRepository.findById(idSubcategoria);
        Perfil facilitador = perfilRepository.findByEmail(emailFacilitador);

        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe esa sucategoría en la base de datos.");
        }
        if (facilitador == null) {
            throw new ErrorServicio("No existe ese facilitador en la base de datos.");
        }

        Evento evento = new Evento();

        evento.setNombre(nombre);
        evento.setSubcategoria(respuesta1.get());
        evento.setModalidad(modalidad);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin);
        evento.setDias(dias);
        evento.setHora(hora);
        evento.setDuracion(duracion);

        switch (modalidad) {
            case PRESENCIAL:
                evento.setCupoPresencial(cupoPresencial);
                break;
            case ONLINE:
                evento.setCupoVirtual(cupoVirtual);
                break;
            case MIXTA:
                evento.setCupoPresencial(cupoPresencial);
                evento.setCupoVirtual(cupoVirtual);
                break;
        }

        evento.setValor(valor);
        evento.setFacilitador(facilitador);
        evento.setDescripcion(descripcion);

        eventoRepository.save(evento);
        
        String subject = "Nuevo Evento";
        String text = "Estimad@ " + facilitador.getNombre() + ": \n Se ha creado "
                + "el evento " + evento.getNombre() + "en el cual sos el facilitador.";

        emailService.enviarCorreo(facilitador.getEmail(), subject, text);

    }

    @Transactional
    public void modificarEvento(Integer id, String nombre, Integer idSubcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            List<Dia> dias, LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, String emailFacilitador,
            String descripcion) throws ErrorServicio, MessagingException {

        boolean crear = false;

        validarDatos(crear, nombre, idSubcategoria, modalidad, fechaInicio, fechaFin,
                hora, duracion, cupoPresencial, cupoVirtual, valor, emailFacilitador,
                descripcion);

        Optional<Evento> respuesta = eventoRepository.findById(id);
        Optional<Subcategoria> respuesta1 = subcategoriaRepository.findById(idSubcategoria);
        Perfil facilitador = perfilRepository.findByEmail(emailFacilitador);

        if (!respuesta.isPresent()) {
            throw new ErrorServicio("No se encontró el evento en la base de datos.");
        }
        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe esa sucategoría en la base de datos.");
        }
        if (facilitador == null) {
            throw new ErrorServicio("No existe ese facilitador en la base de datos.");
        }

        Evento evento = respuesta.get();

        evento.setNombre(nombre);
        evento.setSubcategoria(respuesta1.get());
        evento.setModalidad(modalidad);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin);
        evento.setDias(dias);
        evento.setHora(hora);
        evento.setDuracion(duracion);

        switch (modalidad) {
            case PRESENCIAL:
                evento.setCupoPresencial(cupoPresencial);
                break;
            case ONLINE:
                evento.setCupoVirtual(cupoVirtual);
                break;
            case MIXTA:
                evento.setCupoPresencial(cupoPresencial);
                evento.setCupoVirtual(cupoVirtual);
                break;
        }

        evento.setValor(valor);
        evento.setDescripcion(descripcion);

        if (!evento.getFacilitador().equals(facilitador)) {
            
            String subject = "Nuevo Evento";
            String text = "Estimad@ " + facilitador.getNombre() + ": \n Se ha modificado "
                    + "el evento " + evento.getNombre() + ", en el cual sos el facilitador.";

            emailService.enviarCorreo(facilitador.getEmail(), subject, text);
        }

        evento.setFacilitador(facilitador);

        eventoRepository.save(evento);

    }

    @Transactional
    public List<Evento> buscarTodosLosEventos() throws ErrorServicio {

        return eventoRepository.findAll();

    }

    @Transactional
    public List<Evento> buscarEventoPorNombre(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del evento no puede ser nulo");
        }

        return eventoRepository.buscarPorNombre("%" + nombre + "%");
    }

    @Transactional
    public List<Evento> buscarEventoPorSubcategoria(Integer idSubcategoria) throws ErrorServicio {

        if (idSubcategoria == null) {
            throw new ErrorServicio("La subcategoría del evento no puede ser nula");
        }

        Optional<Subcategoria> respuesta1 = subcategoriaRepository.findById(idSubcategoria);

        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe esa sucategoría en la base de datos.");
        }

        return eventoRepository.buscarPorSubcategoria(idSubcategoria);
    }

    @Transactional
    public List<Evento> buscarEventoPorFacilitador(String emailFacilitador) throws ErrorServicio {

        if (emailFacilitador == null || emailFacilitador.isEmpty()) {
            throw new ErrorServicio("El facilitador del evento no puede ser nulo");
        }

        Perfil facilitador = perfilRepository.findByEmail(emailFacilitador);

        return eventoRepository.buscarPorFacilitador(emailFacilitador);
    }

    @Transactional
    public List<Evento> buscarEventoPorModalidad(Modalidad modalidad) throws ErrorServicio {

        if (modalidad == null) {
            throw new ErrorServicio("La modalidad del evento no puede ser nula");
        }

        return eventoRepository.buscarPorModalidad(modalidad);
    }

    @Transactional
    public List<Evento> buscarEventoPorFecha(LocalDate fechaInicio) throws ErrorServicio {

        if (fechaInicio == null) {
            throw new ErrorServicio("La fecha de inicio del evento no puede ser nula");
        }

        return eventoRepository.findByFechaInicio(fechaInicio);
    }
    
    @Transactional
    public int porcentajeCapacidad(Integer idEvento, Modalidad modalidad) throws ErrorServicio {

        int cantidadInscripciones = inscripcionRepository.cantidadInscripciones(idEvento, modalidad, Arrays.asList(Estado.PENDIENTE, Estado.CONFIRMADO));
        int cupo;
        int porcentajeCupo;        
        
        Optional<Evento> respuesta = eventoRepository.findById(idEvento);
        
        if (!respuesta.isPresent()) {
            throw new ErrorServicio("No se encontró el evento en la base de datos.");
        }
        
        Evento evento = respuesta.get();
        
        if (modalidad.equals(Modalidad.PRESENCIAL)) {
            cupo = evento.getCupoPresencial();
        } else {
            cupo = evento.getCupoVirtual();
        }        
        
        porcentajeCupo = (int)(Math.round(cantidadInscripciones/cupo*100));
        
        return porcentajeCupo;
    }
    
    @Transactional
    public String indicadorCapacidad(Integer idEvento, Modalidad modalidad) throws ErrorServicio {

        int cantidadInscripciones = inscripcionRepository.cantidadInscripciones(idEvento, modalidad, Arrays.asList(Estado.PENDIENTE, Estado.CONFIRMADO));
        int cupo;
        String indicadorCupo;        
        
        Optional<Evento> respuesta = eventoRepository.findById(idEvento);
        
        if (!respuesta.isPresent()) {
            throw new ErrorServicio("No se encontró el evento en la base de datos.");
        }
        
        Evento evento = respuesta.get();
        
        if (modalidad.equals(Modalidad.PRESENCIAL)) {
            cupo = evento.getCupoPresencial();
        } else {
            cupo = evento.getCupoVirtual();
        }        
        
        indicadorCupo = cantidadInscripciones + "/" + cupo;
        
        return indicadorCupo;
    }
    
    
    @Transactional
    public void eliminarEventoPorId(Integer id) throws ErrorServicio {

        Optional<Evento> respuesta = eventoRepository.findById(id);
        if (respuesta.isPresent()) {

            Evento evento = respuesta.get();

            if (evento.getInscripciones().isEmpty()) {
                eventoRepository.deleteById(id);
            } else {
                throw new ErrorServicio("No se puede eliminar un evento con inscripciones.");
            }

        } else {
            throw new ErrorServicio("No hay ningún evento con ese nombre");
        }

    }

    public void validarDatos(boolean crear, String nombre, Integer idSubcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, String emailFacilitador,
            String descripcion) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Debe agregar un nombre al evento.");
        }

        if (idSubcategoria == null) {
            throw new ErrorServicio("Debe agregar una subcategoría al evento.");
        }

        if (modalidad == null) {
            throw new ErrorServicio("Debe indicar modalidad al evento.");
        }

        if (fechaInicio == null) {
            throw new ErrorServicio("Debe indicar la fecha de inicio del evento.");
        }
        if (crear) {
            if (fechaInicio.isBefore(LocalDate.now())) {
                throw new ErrorServicio("La fecha de inicio del evento no puede ser "
                        + "anterior a la fecha actual.");
            }
            if (fechaFin.isBefore(LocalDate.now())) {
                throw new ErrorServicio("La fecha de finalización del evento no puede ser "
                        + "anterior a la fecha actual.");
            }
        }
        if (fechaFin != null) {
            if (fechaInicio.isAfter(fechaFin)) {
                throw new ErrorServicio("La fecha de inicio no puede ser posterior a "
                        + "la fecha de finalización del evento.");
            }
            if (fechaFin.isBefore(fechaInicio)) {
                throw new ErrorServicio("La fecha de finalización no puede ser anterior a "
                        + "la fecha de inicio del evento.");
            }
        }

        if (hora == null) {
            throw new ErrorServicio("Debe indicar la hora del evento.");
        }

        if (duracion == null) {
            throw new ErrorServicio("Debe indicar la duración del evento.");
        }

        switch (modalidad) {
            case PRESENCIAL:
                if (cupoPresencial == null || cupoPresencial < 1) {
                    throw new ErrorServicio("El cupo presencial del evento debe "
                            + "ser un número mayor que cero.");
                }
                break;
            case ONLINE:
                if (cupoVirtual == null || cupoVirtual < 1) {
                    throw new ErrorServicio("El cupo virtual del evento debe "
                            + "ser un número mayor que cero.");
                }
                break;
            case MIXTA:
                if (cupoPresencial == null || cupoVirtual == null
                        || cupoPresencial < 1 || cupoVirtual < 1) {
                    throw new ErrorServicio("Los cupos presencial y virtual del "
                            + "evento deben ser mayores que cero.");
                }
                break;
        }

        if (valor == null || valor < 0) {
            throw new ErrorServicio("Si el evento es gratuito, debe colocar '0'.");
        }

        if (emailFacilitador == null || emailFacilitador.isEmpty()) {
            throw new ErrorServicio("Debe indicar el facilitador del evento.");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new ErrorServicio("Debe agregar una descripción del evento.");
        }
    }

}
