package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Rol;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.repository.RolRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import com.m8.event.manager.service.RolService;
import com.m8.event.manager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestController {

    @GetMapping()
    public String prueba(@RequestParam(value="name", defaultValue="Usuario") String name){
        return new String(String.format("Hola %s",name));
    }


    /*
    @Autowired
    RolService rs = new RolService();

    @Autowired
    private RolRepository rp;


    @GetMapping("/roles/ver")
    public List<Rol> verRoles(){
         return  rp.findAll();
    }

    @PostMapping("/roles/crear")
    public Rol crearRol(@RequestBody Rol nuevoRol){
        return  rp.save(nuevoRol);
    }
*/


}
