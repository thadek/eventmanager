package com.m8.event.manager.service;

import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Subcategoria;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.enumeration.Modalidad;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.EventoRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    @Autowired
    EventoRepository eventoRepository;

    @Transactional
    public void crearEvento(String nombre, Subcategoria subcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            List<Dia> dias, LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, Usuario facilitador,
            String descripcion) throws ErrorServicio {

        validarDatos(nombre, subcategoria, modalidad, fechaInicio, fechaFin,
                hora, duracion, cupoPresencial, cupoVirtual, valor, facilitador,
                descripcion);

        Evento evento = new Evento();

        evento.setNombre(nombre);
        evento.setSubcategoria(subcategoria);
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
        evento.setFacilitador(facilitador);
        evento.setDescripcion(descripcion);

        eventoRepository.save(evento);

    }

    @Transactional
    public void modificarEvento(Integer id, String nombre, Subcategoria subcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            List<Dia> dias, LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, Usuario facilitador,
            String descripcion) throws ErrorServicio {

        validarDatos(nombre, subcategoria, modalidad, fechaInicio, fechaFin,
                hora, duracion, cupoPresencial, cupoVirtual, valor, facilitador,
                descripcion);

        Optional<Evento> respuesta = eventoRepository.findById(id);

        if (respuesta.isPresent()) {
            Evento evento = respuesta.get();

            evento.setNombre(nombre);
            evento.setSubcategoria(subcategoria);
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
            evento.setFacilitador(facilitador);
            evento.setDescripcion(descripcion);

            eventoRepository.save(evento);

        } else {
            throw new ErrorServicio("No se encontró el evento en la base de datos.");
        }
    }

    public void validarDatos(String nombre, Subcategoria subcategoria,
            Modalidad modalidad, LocalDate fechaInicio, LocalDate fechaFin,
            LocalTime hora, Integer duracion, Integer cupoPresencial,
            Integer cupoVirtual, Integer valor, Usuario facilitador,
            String descripcion) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Debe agregar un nombre al evento.");
        }

        if (subcategoria == null) {
            throw new ErrorServicio("Debe agregar una subcategoría al evento.");
        }

        if (modalidad == null) {
            throw new ErrorServicio("Debe indicar modalidad al evento.");
        }

        if (fechaInicio == null) {
            throw new ErrorServicio("Debe indicar la fecha de inicio del evento.");
        }
        if (fechaInicio.isAfter(LocalDate.now())) {
            throw new ErrorServicio("La fecha de inicio del evento no puede ser "
                    + "anterior a la fecha actual.");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new ErrorServicio("La fecha de inicio no puede ser posterior a "
                    + "la fecha de finalización del evento.");
        }
        if (fechaFin.isBefore(LocalDate.now())) {
            throw new ErrorServicio("La fecha de finalización del evento no puede ser "
                    + "anterior a la fecha actual.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ErrorServicio("La fecha de finalización no puede ser anterior a "
                    + "la fecha de inicio del evento.");
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

        if (facilitador == null) {
            throw new ErrorServicio("Debe indicar el facilitador del evento.");
        }

        if (descripcion == null || descripcion.isEmpty()) {
            throw new ErrorServicio("Debe agregar una descripción del evento.");
        }
    }

}
