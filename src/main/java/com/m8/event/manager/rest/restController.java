package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Rol;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.RolRepository;
import com.m8.event.manager.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class restController {

    @GetMapping()
    public String prueba(@RequestParam(value="name", defaultValue="Usuario") String name){
        return new String(String.format("Hola %s",name));
    }



    @Autowired
    private RolService rs;

    @Autowired
    private RolRepository rp;


    @GetMapping("/roles/ver")
    public List<Rol> verRoles(){
         return  rp.findAll();
    }


    @GetMapping("/roles/ver/{id}")
    public Rol verRol(@PathVariable("id") Integer id){
        return rp.findById(id).get();
    }

    @PostMapping("/roles/crear")
    public HashMap crearRol(@RequestBody Rol nuevoRol)  {
        HashMap<String, String> respuesta = new HashMap<>();
        try{
            Rol rol =rs.crearRol(nuevoRol.getNombreRol());
            respuesta.put("respuesta","Rol creado exitosamente.");
            respuesta.put("idRol",rol.getIdRol().toString());
            respuesta.put("nombreRol",rol.getNombreRol());
            return respuesta;
        }catch(ErrorServicio e){
            respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
            return respuesta;
        }


    }

    @PostMapping("/roles/modificar")
    public HashMap modificarRol(@RequestBody Rol rol)  {
        HashMap<String, String> map = new HashMap<>();
        try{

            rs.modificarRol(rol.getNombreRol(),rol.getIdRol());
            map.put("respuesta","Rol modificado correctamente.");
            return map;
        }catch(ErrorServicio e){
            map.put("respuesta","Error al modificar:"+e.getMessage());

            return map;
        }


    }

    @PostMapping("/roles/eliminar")
    public HashMap eliminarRol(@RequestBody Rol rol) {
        HashMap<String, String> map = new HashMap<>();
        try{
            rp.delete(rol);
            map.put("respuesta","Rol eliminado correctamente.");
            return map;
        }catch(Exception e){
            map.put("respuesta","Error al eliminar");
            return map;
        }

    }


}
