package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Inscripcion;
import com.m8.event.manager.enumeration.Estado;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.InscripcionRepository;
import com.m8.event.manager.repository.PerfilRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import com.m8.event.manager.service.EventoService;
import com.m8.event.manager.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class inscripcionRestController {


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

    /* ABM INSCRIPCIONES */


    @PostMapping("/inscripciones/nueva")
    public HashMap crearInscripcion (@RequestBody Inscripcion insc) throws Exception{
        HashMap<String,String> respuesta = new HashMap<>();

        /*REVISAR INSCRIPCION SERVICE**/
        //  System.out.println("Inscripcion recibida: "+insc);

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
            return respuesta;
        }catch(Exception e){
            respuesta.put("error","Ocurrió un error: "+e.getMessage());
            return respuesta;
        }

    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/inscripciones/pendientes")
    public List<Inscripcion> verInscripcionesPendientes(){

        try{
            return ir.inscripcionesPendientes();
        }catch(Exception e){
            return null;
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/inscripciones/confirmar/{id}")
    public HashMap confirmarInscripcion(@PathVariable("id") Integer id){
        HashMap <String,String> respuesta = new HashMap<>();

        try{
            Inscripcion insc = ir.findById(id).get();
            is.modificarInscripcion(id,insc.getEvento().getId(),insc.getAlumno().getEmail(),insc.getModalidad(), Estado.CONFIRMADO);
            respuesta.put("respuesta","Se confirmo la inscripción y se notificó por correo electrónico al alumno.");
            return respuesta;
        }catch(Exception e){
            respuesta.put("error","Ocurrió un error: "+e.getMessage());
            return respuesta;
        }

    }








    @PreAuthorize("#username==authentication.principal.username or hasRole('ROLE_ADMIN')"  )
    @GetMapping("/inscripciones/{evento}/{username}")
    public HashMap verificarInscripcionPorUsername(@PathVariable("evento")Integer idEvento, @PathVariable("username") String username){
        HashMap<String,String> res = new HashMap<>();
        try{
            Evento ev=  erp.findById(idEvento).get();
            for (Inscripcion i:ev.getInscripciones()) {
                if(i.getAlumno().getUsuario().getUsername().equals(username)){
                    res.put("respuesta","ok");
                    res.put("estado",i.getEstado().toString());
                    break;
                }
            }
            return res;
        }catch(Exception e){
            res.put("respuesta","error");
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

    @GetMapping("/inscripciones/{idevento}")
    public List<Inscripcion> verListaDeInscripcionesDeUnEvento(@PathVariable("idevento")Integer idevento){
        return erp.findById(idevento).get().getInscripciones();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/inscripciones/buscarporid/{id}")
    public Inscripcion buscarInscripcionPorId (@PathVariable("id") Integer idinsc){
        try{
           return ir.findById(idinsc).get();
        }catch(Exception e){
            return null;
        }
    }

    @PreAuthorize("#username==authentication.principal.username")
    @DeleteMapping("/baja/{idevento}/{username}")
    public HashMap darDeBajaInscripcion(@PathVariable("username") String username, @PathVariable("idevento") Integer idevento){
        HashMap<String,String> respuesta = new HashMap<>();
        try{
            List<Inscripcion> inscripcionesAlumno = ir.inscripcionesPorAlumno(username);
            inscripcionesAlumno.forEach(inscripcion -> {
                if(inscripcion.getEvento().getId() == idevento){
                    ir.delete(inscripcion);
                    respuesta.put("respuesta","Se dio de baja tu inscripción correctamente.");

                }
            });
            return respuesta;

        }catch(Exception e){
            respuesta.put("respuesta", "Ocurrió un error: "+e.getMessage());
            respuesta.put("error","true");
            return respuesta;
        }

    }







}
