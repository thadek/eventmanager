package com.m8.event.manager.rest;


import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.error.ErrorServicio;

import com.m8.event.manager.repository.UsuarioRepository;

import com.m8.event.manager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/api/usuario")
public class usuarioRestController {



        @Autowired
        private BCryptPasswordEncoder encoder;

        @Autowired
        private UsuarioService us;

        @Autowired
        private UsuarioRepository ur;

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @GetMapping("/ver")
        public List<Usuario> verUsuarios(){
            return  ur.findAll();
        }

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @GetMapping("/ver/{username}")
        public Usuario verUser(@PathVariable("username") String username){
            return ur.findById(username).get();
        }

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @PostMapping("/crear")
        public HashMap crearUsuario(@RequestBody Usuario nuevoUser)  {
            HashMap<String, String> respuesta = new HashMap<>();
            try{
                Usuario user = us.crearUsuario(nuevoUser.getUsername(), nuevoUser.getPassword(),
                      nuevoUser.getPassword(), nuevoUser.getRol().getIdRol());
                respuesta.put("respuesta","Usuario creado exitosamente.");
                respuesta.put("username",user.getUsername() );
                respuesta.put("rol",user.getRol().getNombreRol());
                return respuesta;
            }catch(ErrorServicio e){
                respuesta.put("respuesta","Ocurrió un error: "+e.getMessage());
                return respuesta;
            }


        }

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @PostMapping("/modificar")
        public HashMap modificarUsuario(@RequestBody Usuario user)  {
            HashMap<String, String> map = new HashMap<>();
            try{
                us.modificarUsuario(user.getUsername(),user.getPassword(), user.getPassword(), user.getRol().getIdRol());
                map.put("respuesta","Usuario modificado correctamente.");
                return map;
            }catch(ErrorServicio e){
                map.put("respuesta","Error al modificar:"+e.getMessage());
                return map;
            }
        }

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @DeleteMapping("/eliminar")
        public HashMap eliminarUsuario(@RequestBody Usuario user) {
            HashMap<String, String> map = new HashMap<>();
            try{
                ur.delete(user);
                map.put("respuesta","Usuario eliminado correctamente.");
                return map;
            }catch(Exception e){
                map.put("respuesta","Error al eliminar");
                return map;
            }
        }

    }

