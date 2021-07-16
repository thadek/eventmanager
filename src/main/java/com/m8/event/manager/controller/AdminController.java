package com.m8.event.manager.controller;

import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.repository.CategoriaRepository;
import com.m8.event.manager.repository.PerfilRepository;
import com.m8.event.manager.repository.RolRepository;
import com.m8.event.manager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adm")
public class AdminController {

    @GetMapping
    public ModelAndView interfazAdm() {
        ModelAndView mav = new ModelAndView("adm");
        mav.addObject("title","Administración - EventManager ");
        return mav;
    }

    @GetMapping("/roles")
    public ModelAndView verRoles(){
        ModelAndView mav = new ModelAndView("rol");
        mav.addObject("title","Roles de Usuario - EventManager ");
        return mav;
    }

    @GetMapping("/categorias")
    public ModelAndView verCategorias(){
        ModelAndView mav = new ModelAndView("categoria");
        mav.addObject("title","Categorias de Evento - EventManager ");
        return mav;
    }

    @GetMapping("/eventos")
    public ModelAndView verEventos(){
        ModelAndView mav = new ModelAndView("eventoadm");
        mav.addObject("title","Gestion de Eventos - EventManager ");
        return mav;
    }



    @Autowired
    private PerfilRepository pr;

    @GetMapping("/perfil/ver/{username}")
    public ModelAndView verCategorias(@PathVariable("username") String username){
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        ModelAndView mav = new ModelAndView("perfiladm");
        mav.addObject("title","Gestion de Perfil - EventManager ");
        mav.addObject("usuario",pr.findByUsuario(usuario));
        return mav;
    }

    @Autowired
    private CategoriaRepository cr;

    @GetMapping("/subcategorias")
    public ModelAndView verSubcategorias(){
        ModelAndView mav = new ModelAndView("subcategoria");
        mav.addObject("title","Subcategorias de Evento - EventManager ");
        mav.addObject("listaCategorias",cr.findAll());
        return mav;
    }


    @Autowired
    private RolRepository rr;

    @GetMapping("/usuarios")
    public ModelAndView verUsuarios(){
        ModelAndView mav = new ModelAndView("usuario");
        mav.addObject("title","Usuarios - EventManager ");
        mav.addObject("roles",rr.findAll());
        return mav;
    }


}
