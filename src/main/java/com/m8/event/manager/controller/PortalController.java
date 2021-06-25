package com.m8.event.manager.controller;

import com.m8.event.manager.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
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
