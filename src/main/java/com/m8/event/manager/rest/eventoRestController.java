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



    /*Controladores para revisar ocupación en eventos */

    @GetMapping("/ocupacion/{idevento}/mixta")
    public HashMap getOcupacionMixta(@PathVariable("idevento")Integer idevento){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            respuesta.put("indicadorOnline",es.indicadorCapacidad(idevento, Modalidad.ONLINE));
            respuesta.put("porcentajeOnline", Integer.toString(es.porcentajeCapacidad(idevento,Modalidad.ONLINE)));
            respuesta.put("indicadorPresencial",es.indicadorCapacidad(idevento, Modalidad.PRESENCIAL));
            respuesta.put("porcentajePresencial", Integer.toString(es.porcentajeCapacidad(idevento,Modalidad.PRESENCIAL)));
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
    @GetMapping("/inscripciones/{evento}/{username}")
    public HashMap verificarInscripcionPorUsername(@PathVariable("evento")Integer idEvento, @PathVariable("username") String username){
        HashMap<String,String> res = new HashMap<>();
       try{
          Evento ev=  erp.findById(idEvento).get();
           for (Inscripcion i:ev.getInscripciones()) {
               if(i.getAlumno().getUsuario().getUsername().equals(username)){
                   res.put("respuesta","true");
                   break;
               }
           }
           return res;
       }catch(Exception e){

           res.put("respuesta","false");
           return res;
       }
    }


    @PreAuthorize ("#username==authentication.principal.username or hasRole('ROLE_ADMIN')"  )
    @GetMapping("/inscripciones/user/{username}")
    public List<Inscripcion> listaDeInscripcionesPorUserName(@PathVariable("username") String username){
    //Lista de inscripciones de un usuario específico
        try{
            return ir.inscripcionesPorAlumno(pr.findByUsuario(ur.findByUsername(username)).getEmail());
        }catch(Exception e){
            return null;
        }
    }

    @PreAuthorize ("#username==authentication.principal.username or hasRole('ROLE_ADMIN')"  )
    @GetMapping("/miseventos/{username}")
    public List<Evento> misEventos(@PathVariable("username") String username){
        //Lista de EVENTOS A LOS QUE UN USER ESTA INSCRIPTO.
        List<Evento> listaEventosUsuario = new ArrayList<>();
        try{
        for (Inscripcion insc: ir.inscripcionesPorAlumno(pr.findByUsuario(ur.findByUsername(username)).getEmail())) {
            listaEventosUsuario.add(insc.getEvento());
        }

        return listaEventosUsuario;

        }catch(Exception e){
            return null;
        }
    }


    @GetMapping("/inscripciones/{idevento}")
    public List<Inscripcion> verListaDeInscripcionesDeUnEvento(@PathVariable("idevento")Integer idevento){
       return erp.findById(idevento).get().getInscripciones();
    }



    /* ABM INSCRIPCIONES */

    @PostMapping("/inscripciones/nueva")
    public HashMap crearInscripcion (@RequestBody Inscripcion insc) throws Exception{
        HashMap<String,String> respuesta = new HashMap<>();

/*REVISAR INSCRIPCION SERVICE**/
            System.out.println("Inscripcion recibida: "+insc);

            is.crearInscripcion(insc.getEvento().getId(),insc.getAlumno().getEmail(),insc.getModalidad());

            respuesta.put("respuesta","Inscripcion realizada correctamente.");
            respuesta.put("error","false");
            return respuesta;


          //  respuesta.put("respuesta");
            //respuesta.put("error","true");
            //return respuesta;


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/inscripciones/editar")
    public HashMap editarInscripcion(@RequestBody Inscripcion insc){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            is.modificarInscripcion(insc.getIdInscripcion(),insc.getEvento().getId(),insc.getAlumno().getEmail(),insc.getModalidad(),insc.getEstado());
            respuesta.put("respuesta","Inscripcion modificada correctamente.");
            respuesta.put("error","false");
            return respuesta;
        }catch(Exception e){
            respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
            respuesta.put("error","true");
            return respuesta;
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/inscripciones/eliminar")
    public HashMap eliminarInscripcion(@RequestBody Inscripcion insc){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            ir.delete(insc);
            respuesta.put("respuesta","Inscripción eliminada correctamente.");
            respuesta.put("error","false");
            return respuesta;
        }catch(Exception e){
            respuesta.put("error","true");
            respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
            return respuesta;
        }

    }






     /*  ABM EVENTO    */

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
    @DeleteMapping("/eliminar")
    public HashMap eliminarEvento(@RequestBody Evento ev){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            es.eliminarEventoPorId(ev.getId());
            respuesta.put("respuesta","Evento Eliminado exitosamente. Se envió un correo electrónico al facilitador.");
            return respuesta;
        }catch(Exception e){
            respuesta.put("respuesta","Ocurrio un error: "+e.getMessage());
            return respuesta;
        }


    }





}
