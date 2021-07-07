package com.m8.event.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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




}
