package com.m8.event.manager.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.entity.Rol;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.PerfilRepository;
import com.m8.event.manager.repository.RolRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import com.m8.event.manager.service.PerfilService;
import com.m8.event.manager.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping ("/api/perfil")
public class perfilController {

   @Autowired
   private PerfilService ps;

   @Autowired
   private PerfilRepository pr;

   @Autowired
   private UsuarioRepository ur;


   //Este id referencia la ID de usuario no al ID de perfil. La notación preauthorize de este método SOLO VALIDA SI EL USUARIO SOLICITANTE ES EL MISMO QUE SE PIDE, ES DECIR UN USUARIO SÓLO PUEDE VER SU PROPIO PERFIL.
   @PreAuthorize ("#username==authentication.principal.username or hasRole('ROLE_ADMIN')")
   @GetMapping ("/ver/{username}")
   public Perfil verPerfil (@PathVariable ("username") String username) {
      Usuario u = new Usuario ();
      u.setUsername (username);
      try {
         return pr.findByUsuario (u);
      } catch (Exception e) {
         return null;
      }
   }



   @GetMapping("/ver/foto/{username}")
   public HashMap verFotoPerfil(@PathVariable("username")String username){
      HashMap<String, String> respuesta = new HashMap<> ();

      Usuario u = new Usuario ();
      u.setUsername (username);
      try{
           respuesta.put("foto",pr.findByUsuario(u).getFotoURL());
           return respuesta;
      }catch(Exception e){
         respuesta.put("foto","https://i.imgur.com/tRLkAZB.png");
         respuesta.put("Error","no hay foto asignada a ese usuario o no existe");
         return respuesta;
      }

   }


   @PreAuthorize ("#username==authentication.principal.username or hasRole('ROLE_ADMIN')")
   @PostMapping ("/crear/{username}")
   public HashMap crearPerfil (@PathVariable ("username") String username,@RequestBody Perfil nuevoPerfil) {
      HashMap<String, String> respuesta = new HashMap<> ();
      try {
         Perfil p = ps.agregarPerfil (nuevoPerfil.getUsuario ().getUsername (), nuevoPerfil.getFotoURL (),
               nuevoPerfil.getEmail (), nuevoPerfil.getNombre (), nuevoPerfil.getApellido (), nuevoPerfil.getTel (),
               nuevoPerfil.getFechaNac (), nuevoPerfil.getDescripcion ());

         respuesta.put ("respuesta", "Perfil creado exitosamente.");
         respuesta.put ("idPerfil", p.getIdPerfil ().toString ());

         return respuesta;
      } catch (Exception e) {
         respuesta.put ("respuesta", "Ocurrió un error: " + e.getMessage ());
         return respuesta;
      }

   }

   @PreAuthorize ("#username==authentication.principal.username or hasRole('ROLE_ADMIN')"  )
   @PostMapping ("/modificar/{username}")
   public HashMap modificarPerfil (@PathVariable ("username") String username, @RequestBody Perfil p ) {
      HashMap<String, String> map = new HashMap<> ();
      try {
         ps.modificar (p.getIdPerfil (), p.getFotoURL (), p.getEmail (), p.getNombre (), p.getApellido (),
               p.getTel (), p.getFechaNac (), p.getDescripcion ());
         map.put ("respuesta", "Perfil modificado correctamente.");
         return map;
      } catch (ErrorServicio e) {
         map.put ("respuesta", "Error al modificar:" + e.getMessage ());
         return map;
      }
   }

   @PreAuthorize ("hasRole('ADMIN')")
   @DeleteMapping ("/eliminar")
   public HashMap eliminarPerfil (@RequestBody Perfil p) {
      HashMap<String, String> map = new HashMap<> ();
      try {
         pr.delete (p);
         map.put ("respuesta", "Perfil eliminado correctamente.");
         return map;
      } catch (Exception e) {
         map.put ("respuesta", "Error al eliminar");
         return map;
      }
   }

}
