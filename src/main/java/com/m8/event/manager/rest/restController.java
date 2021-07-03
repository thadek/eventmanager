package com.m8.event.manager.rest;


import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class restController {

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
