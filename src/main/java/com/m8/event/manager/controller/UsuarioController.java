package com.m8.event.manager.controller;

import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.RolRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import com.m8.event.manager.service.RolService;
import com.m8.event.manager.service.UsuarioService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolService rolService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;

    @GetMapping("/crear")
    public ModelAndView crearUsuario(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("usuarios-formulario");
        mav.addObject("title", "Crear Usuario");
        mav.addObject("action", "crear");
        Usuario usuario = new Usuario();

        mav.addObject("roles", rolRepository.findAll());

        Map<String, ?> flashmap = RequestContextUtils.getInputFlashMap(request);
        if (flashmap != null) {
            mav.addObject("mensaje", flashmap.get("usuarioCreado"));
            mav.addObject("error", flashmap.get("error"));
            usuario.setUsername((String) flashmap.get("username"));
            usuario.setPassword((String) flashmap.get("password"));            
        }

        mav.addObject("usuario", usuario);
        return mav;
    }

    @PostMapping("/crear")
    public RedirectView registrar(RedirectAttributes attributes,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam(value = "rol", required = false) Integer idRol) {
        try {
            usuarioService.crearUsuario(username, password, password2, idRol);
            attributes.addFlashAttribute("registroExitoso", "El Usuario fue creado con éxito.");
        } catch (ErrorServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());
            attributes.addFlashAttribute("username", username);
            attributes.addFlashAttribute("rol", idRol);
            attributes.addFlashAttribute("password", password);
            return new RedirectView("/usuarios-formulario");
        }
        return new RedirectView("/login?register=ok");
    }

    @GetMapping("/modificar/{username}")
    public ModelAndView modificarUsuario(@PathVariable String username) {
        ModelAndView mav = new ModelAndView("usuarios-formulario");
        mav.addObject("title", "Modificar Usuario");
        mav.addObject("action", "modificar");

        mav.addObject("usuario", usuarioRepository.findByUsername(username));
        mav.addObject("roles", rolRepository.findAll());

        return mav;
    }

//    @GetMapping("/editar/{documento}")
//    public ModelAndView editarUsuario(@PathVariable Long documento, Principal principal) {
//        String email = principal.getName();
//        ModelAndView mav = new ModelAndView("usuarios-formulario");
//        mav.addObject("title", "Modificar Usuario");
//        mav.addObject("action", "modificar");
//        try {
//            mav.addObject("usuario", usuarioServicio.buscarUsuarioPorDoc(documento));
//            mav.addObject("perfiles", perfilServicio.buscarTodosLosPerfiles());
//        } catch (ErrorServicio e) {
//            mav.addObject("error", e.getMessage());
//        }
//
//        return mav;
//    }

    @PostMapping("/modificar")
    public RedirectView modificarUsuario(RedirectAttributes attributes,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam(value = "rol", required = false) Integer idRol) {
        try {
            usuarioService.modificarUsuario(username, password, password2, idRol);
            attributes.addFlashAttribute("usuarioModificado", "El Usuario fue modificado con éxito.");
        } catch (ErrorServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());
            attributes.addFlashAttribute("username", username);
            attributes.addFlashAttribute("rol", idRol);
            attributes.addFlashAttribute("password", password);

            return new RedirectView("/usuarios/modificar/" + username);
        }
        return new RedirectView("/index");
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView buscarUsuario(@RequestParam(required = false) String username) {
        ModelAndView mav = new ModelAndView("usuarios-lista");

        Usuario usuario = new Usuario();
        List<Usuario> usuarios = new ArrayList();

        usuario = usuarioRepository.findByUsername(username);
        usuarios.add(usuario);
        mav.addObject("listaDeUsuarios", usuarios);

        return mav;
    }

    @GetMapping("/ver-todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView verTodos() {
        
        ModelAndView mav = new ModelAndView("usuarios-lista");
        mav.addObject("title", "Lista de Usuarios");
        
        List<Usuario> usuarios = usuarioRepository.findAll();
        mav.addObject("listaDeUsuarios", usuarios);
        
        return mav;
    }

//    @PostMapping("/eliminar/{documento}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public RedirectView eliminar(@PathVariable Long documento, RedirectAttributes attributes) {
//
//        try {
//            usuarioServicio.eliminarUsuarioPorDocumento(documento);
//        } catch (ErrorServicio e) {
//            attributes.addFlashAttribute("error", e.getMessage());
//        }
//
//        return new RedirectView("/usuarios/ver-todos");
//    }    
}
