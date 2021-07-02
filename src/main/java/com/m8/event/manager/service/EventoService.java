package com.m8.event.manager.service;

import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Subcategoria;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.enumeration.Modalidad;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.SubcategoriaRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public void crearEvento(String nombre, Integer idSubcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            List<Dia> dias, LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, String facilitador,
            String descripcion) throws ErrorServicio {

        boolean crear = true;

        validarDatos(crear, nombre, idSubcategoria, modalidad, fechaInicio, fechaFin,
                hora, duracion, cupoPresencial, cupoVirtual, valor, facilitador,
                descripcion);

        Optional<Subcategoria> respuesta1 = subcategoriaRepository.findById(idSubcategoria);
        Optional<Usuario> respuesta2 = usuarioRepository.findById(facilitador);

        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe esa sucategoría en la base de datos.");
        }
        if (!respuesta2.isPresent()) {
            throw new ErrorServicio("No existe ese facilitador en la base de datos.");
        }

        Evento evento = new Evento();

        evento.setNombre(nombre);
        evento.setSubcategoria(respuesta1.get());
        evento.setModalidad(modalidad);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin); //Consultar qué pasa si fechaFin es null
        evento.setDias(dias);//Consultar qué pasa si la lista viene vacía o null
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
        evento.setFacilitador(respuesta2.get());
        evento.setDescripcion(descripcion);

        eventoRepository.save(evento);

    }

    @Transactional
    public void modificarEvento(Integer id, String nombre, Integer idSubcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            List<Dia> dias, LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, String facilitador,
            String descripcion) throws ErrorServicio {

        boolean crear = false;

        validarDatos(crear, nombre, idSubcategoria, modalidad, fechaInicio, fechaFin,
                hora, duracion, cupoPresencial, cupoVirtual, valor, facilitador,
                descripcion);

        Optional<Evento> respuesta = eventoRepository.findById(id);
        Optional<Subcategoria> respuesta1 = subcategoriaRepository.findById(idSubcategoria);
        Optional<Usuario> respuesta2 = usuarioRepository.findById(facilitador);

        if (respuesta.isPresent()) {
            throw new ErrorServicio("No se encontró el evento en la base de datos.");
        }
        if (!respuesta1.isPresent()) {
            throw new ErrorServicio("No existe esa sucategoría en la base de datos.");
        }
        if (!respuesta2.isPresent()) {
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
        evento.setFacilitador(respuesta2.get());
        evento.setDescripcion(descripcion);

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

        return eventoRepository.buscarPorNombre(nombre);
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
    public List<Evento> buscarEventoPorFacilitador(String facilitador) throws ErrorServicio {

        if (facilitador == null || facilitador.isEmpty()) {
            throw new ErrorServicio("El facilitador del evento no puede ser nulo");
        }
        
        Optional<Usuario> respuesta2 = usuarioRepository.findById(facilitador);

        return eventoRepository.buscarPorFacilitador(facilitador);
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
    public void eliminarEventoPorId(Integer id) throws ErrorServicio {

        Optional<Evento> respuesta = eventoRepository.findById(id);
        if (respuesta.isPresent()) {
            
            Evento evento = respuesta.get();
            
            if (evento.getInscripciones().isEmpty()) {
                eventoRepository.deleteById(id);
            } else{
                throw new ErrorServicio ("No se puede eliminar un evento con inscripciones.");
            }       

        } else {
            throw new ErrorServicio("No hay ningún evento con ese nombre");
        }

    }


    public void validarDatos(boolean crear, String nombre, Integer idSubcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, String facilitador,
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
            if (fechaInicio.isAfter(LocalDate.now())) {
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

        if (facilitador == null || facilitador.isEmpty()) {
            throw new ErrorServicio("Debe indicar el facilitador del evento.");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new ErrorServicio("Debe agregar una descripción del evento.");
        }
    }

}
