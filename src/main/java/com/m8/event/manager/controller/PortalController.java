package com.m8.event.manager.controller;

import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("title","Inicio - EventManager");
        return mav;
    }


    @GetMapping("/register")
    public ModelAndView register(){
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("title", "Registrarse - EventManager");
        return mav;
    }

    @PostMapping ("/register")
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
            return new RedirectView("/register?error");
        }
        return new RedirectView("/login?register=ok");
    }


    @GetMapping("/login")
    public ModelAndView login(@RequestParam(required = false) String error,
            @RequestParam(required = false) String logout, ModelAndView mav) {
        if (error != null) {
            mav.addObject("errorLogin", "Usuario o Password incorrectos");
        }
        if (logout != null) {
            mav.addObject("logout", "Hasta luego!");
        }
        return new ModelAndView("login");
    }



////    Forma Seba
//    @GetMapping("/login")
//    public String login(@RequestParam(required = false) String error, 
//            @RequestParam(required = false) String logout, ModelMap model){
//        if (error != null) {
//            model.put("errorLogin", "Usuario o Password incorrectos");
//        }
//        if (logout != null) { 
//            model.put("logout", "Hasta luego!");
//        }
//        return "login.html";
//    } 

//    @GetMapping("/home")
//    public ModelAndView home() {
//        return new ModelAndView("home");
//    }
    
    @GetMapping("/error-403")
    public ModelAndView error() {
        return new ModelAndView("error-403");
    }

}
