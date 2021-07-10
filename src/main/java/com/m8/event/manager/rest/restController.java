package com.m8.event.manager.rest;


import com.m8.event.manager.enumeration.Dia;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class restController {

    @GetMapping()
    public String prueba(@RequestParam(value="name", defaultValue="Usuario") String name){
        return new String(String.format("Event Manager - Mesa 8 EGG - ¡Hola %s!",name));
    }


    @RequestMapping("/test/dias")
    public List<Dia> listaDias(){

        List<Dia> dias = new ArrayList<Dia>();

        dias.add(Dia.DOMINGO);
        dias.add(Dia.LUNES);
        dias.add(Dia.MARTES);
        dias.add(Dia.MIERCOLES);
        dias.add(Dia.JUEVES);
        dias.add(Dia.VIERNES);
        dias.add(Dia.SABADO);

        return dias;

    }



}
