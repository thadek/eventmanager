package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Rol;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.RolRepository;
import com.m8.event.manager.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class rolController {

    @Autowired
    private RolService rs;

    @Autowired
    private RolRepository rp;


    @GetMapping("/ver")
    public List<Rol> verRoles(){
        return  rp.findAll();
    }


    @GetMapping("/ver/{id}")
    public Rol verRol(@PathVariable("id") Integer id){
        return rp.findById(id).get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/crear")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modificar")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/eliminar")
    public HashMap eliminarRol(@RequestBody Rol rol) {
        HashMap<String, String> map = new HashMap<>();
        try{
            rp.delete(rol);
            map.put("respuesta","Rol eliminado correctamente.");
            return map;
        }catch(Exception e){
            map.put("respuesta","Error al eliminar, ¿Este rol tiene usuarios asignados?");
            return map;
        }
    }

}
