package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.enumeration.Dia;
import com.m8.event.manager.enumeration.Modalidad;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.InscripcionRepository;
import com.m8.event.manager.repository.PerfilRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import com.m8.event.manager.service.EventoService;
import com.m8.event.manager.service.InscripcionService;
import com.m8.event.manager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/eventos")
public class eventoRestController {

    @Autowired
    private UsuarioRepository ur;

    @Autowired
    private InscripcionRepository ir;

    @Autowired
    private InscripcionService is;

    @Autowired
    private EventoService es;

    @Autowired
    private EventoRepository erp;

    @Autowired
    private PerfilRepository pr;



    @GetMapping("/vertodos")
    public List<Evento> verTodos(){

        try{
            return es.buscarTodosLosEventos();
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/ver/{id}")
    public Evento verEvento(@PathVariable("id")Integer id){
        return erp.findById(id).get();
    }



    /*  ABM EVENTO    */

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/crear")
    public HashMap crearEvento(@RequestBody Evento nuevoEvento) {
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
            respuesta.put("error","true");
            respuesta.put("respuesta","Ocurrio un error:"+e.getMessage());
            return respuesta;
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/eliminar")
    public HashMap eliminarEvento(@RequestBody Evento ev){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            es.eliminarEventoPorId(ev.getId());
            respuesta.put("respuesta","Evento Eliminado exitosamente. Se envió un correo electrónico al facilitador.");
            return respuesta;
        }catch(Exception e){
            respuesta.put("error","true");
            respuesta.put("respuesta","Ocurrio un error: "+e.getMessage());
            return respuesta;
        }


    }




    /*Controladores para revisar ocupación en eventos */

    @GetMapping("/ocupacion/{idevento}/mixta")
    public HashMap getOcupacionMixta(@PathVariable("idevento")Integer idevento){
        HashMap<String,String> respuesta = new HashMap<>();

        try{
            respuesta.put("indicadorOnline",es.indicadorCapacidad(idevento, Modalidad.ONLINE));
            respuesta.put("indicadorPresencial",es.indicadorCapacidad(idevento, Modalidad.PRESENCIAL));
            respuesta.put("porcentaje", Integer.toString(es.porcentajeCapacidadMixto(idevento)));
            respuesta.put("error","false");
            return respuesta;
        }catch(Exception e ){
            respuesta.put("error","true");
            respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
            return respuesta;
        }
    }

    @GetMapping("/ocupacion/{idevento}/presencial")
    public HashMap getOcupacionPresencial(@PathVariable("idevento")Integer idevento){
            HashMap<String,String> respuesta = new HashMap<>();
            try{
               respuesta.put("indicador",es.indicadorCapacidad(idevento, Modalidad.PRESENCIAL));
               respuesta.put("porcentaje", Integer.toString(es.porcentajeCapacidad(idevento,Modalidad.PRESENCIAL)));
               respuesta.put("error","false");
               return respuesta;
            }catch(Exception e ){
                respuesta.put("error","true");
                respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
                return respuesta;
            }
    }

    @GetMapping("/ocupacion/{idevento}/online")
    public HashMap getOcupacionOnline(@PathVariable("idevento")Integer idevento){
            HashMap<String,String> respuesta = new HashMap<>();
            try{
                respuesta.put("indicador",es.indicadorCapacidad(idevento, Modalidad.ONLINE));
                respuesta.put("porcentaje", Integer.toString(es.porcentajeCapacidad(idevento,Modalidad.ONLINE)));
                respuesta.put("error","false");
                return respuesta;
            }catch(Exception e ){
                respuesta.put("error","true");
                respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
                return respuesta;
            }
        }




    /* Inscripciones */



    @PreAuthorize ("#username==authentication.principal.username or hasRole('ROLE_ADMIN')"  )
    @GetMapping("/miseventos/{username}")
    public List<Evento> misEventos(@PathVariable("username") String username){
        //Lista de EVENTOS A LOS QUE UN USER ESTA INSCRIPTO.
        List<Evento> listaEventosUsuario = new ArrayList<>();
        try{
        for (Inscripcion insc: ir.inscripcionesPorAlumno(username)){
            listaEventosUsuario.add(insc.getEvento());
        }

        return listaEventosUsuario;

        }catch(Exception e){
            return null;
        }
    }








}
