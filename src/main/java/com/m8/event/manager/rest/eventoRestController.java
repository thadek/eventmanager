package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.InscripcionRepository;
import com.m8.event.manager.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
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

        try{
            return es.buscarTodosLosEventos();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/inscripciones/{idevento}")
    public List<Inscripcion> verListaDeInscripcionesDeUnEvento(@PathVariable("idevento")Integer idevento){
       return erp.findById(idevento).get().getInscripciones();
    }

    /* Lista de eventos a los que estoy inscripto
    @GetMapping("/miseventos/{username}")
    public List<Evento> listadeEventosPorUsuario(@PathVariable("username")String username){
       try { es.listadeEventosporUsuario(usuario)
       }catch(Exception e){
        return null  }
    }
    */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/crear")
    public HashMap crearEvento(@RequestBody Evento nuevoEvento){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            es.crearEvento(nuevoEvento.getNombre(),nuevoEvento.getSubcategoria().getIdSubcategoria()
                    ,nuevoEvento.getModalidad()
                    ,nuevoEvento.getFechaInicio()
                    ,nuevoEvento.getFechaFin()
                    ,nuevoEvento.getDias()
                    ,nuevoEvento.getHora()
                    ,nuevoEvento.getDuracion()
                    ,nuevoEvento.getCupoPresencial()
                    ,nuevoEvento.getCupoVirtual()
                    ,nuevoEvento.getValor()
                    ,nuevoEvento.getFacilitador().getEmail()
                    ,nuevoEvento.getDescripcion());
            respuesta.put("respuesta","Evento Creado exitosamente.");

            return respuesta;

        }catch(Exception e ){
            respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
            respuesta.put("error","true");
            return respuesta;

        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modificar")
    public HashMap modificarEvento(@RequestBody Evento ev){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            es.modificarEvento(ev.getId(),
                    ev.getNombre(),
                    ev.getSubcategoria().getIdSubcategoria(),
                    ev.getModalidad(),
                    ev.getFechaInicio(),
                    ev.getFechaFin(),
                    ev.getDias(),
                    ev.getHora(),
                    ev.getDuracion(),
                    ev.getCupoPresencial(),
                    ev.getCupoVirtual(),
                    ev.getValor(),
                    ev.getFacilitador().getEmail(),
                    ev.getDescripcion());
            respuesta.put("respuesta","Evento modificado exitosamente.");
            return respuesta;
        }catch(Exception e){
            respuesta.put("respuesta","Ocurrio un error:"+e.getMessage());
            return respuesta;
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/eliminar")
    public HashMap eliminarEvento(@RequestBody Evento ev){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            es.eliminarEventoPorId(ev.getId());
            respuesta.put("respuesta","Evento Eliminado exitosamente");
            return respuesta;
        }catch(Exception e){
            respuesta.put("respuesta","Ocurrio un error: "+e.getMessage());
            return respuesta;
        }


    }


}
