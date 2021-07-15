package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.service.EmailService;
import com.m8.event.manager.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private EmailService em;

    @GetMapping("/correo")
    public String mandaremail(@RequestParam(value="body") String cuerpo,@RequestParam(value="user") String user,@RequestParam(value="ev") String ev,@RequestParam(value="destino") String destino){     try{
           em.enviarNotificacionEvento(destino,"EventManager M8 - Notificacion",cuerpo,user,ev,"Admin");
           return "SE MANDO ALGO, FIJATE K PES";
       }catch(Exception e){
           return "SE ROMPIO ESTO: "+e.getMessage();
       }

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


//    @Autowired
//    private PerfilService ps;
//
//    /* Tana -> para ver instructores*/
//    @GetMapping("/instructores")
//    public List<Perfil> verInstructores(){
//        return ps.verListaDeProfesores ();
//    }

}
