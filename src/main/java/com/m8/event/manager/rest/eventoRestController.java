package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.InscripcionRepository;
import com.m8.event.manager.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/eventos")
public class eventoRestController {

    @Autowired
    private InscripcionRepository is;

    @Autowired
    private EventoService es;

    @Autowired
    private EventoRepository erp;


    @GetMapping()
    public String prueba(@RequestParam(value="name", defaultValue="Usuario") String name){
        return new String(String.format("Event Manager - Mesa 8 EGG - ¡Hola %s!",name));
    }

    @GetMapping("/vertodos")
    public List<Evento> verTodos(){
        return erp.findAll();
    }

    @GetMapping("/verinscripciones")
    public List<Inscripcion> verTodos1(){
        return is.findAll();
    }


}
