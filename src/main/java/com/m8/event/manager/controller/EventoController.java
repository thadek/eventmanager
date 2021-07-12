package com.m8.event.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/eventos")
public class EventoController {


    @GetMapping()
    public ModelAndView eventos(){
        ModelAndView mav = new ModelAndView("eventos");
        mav.addObject("title","Lista de eventos");
        return mav;
    }


    @GetMapping("/ver")
    public ModelAndView verEvento(){
        ModelAndView mav = new ModelAndView("evento-detalle");
        mav.addObject("title","Detalle Evento");
        return mav;
    }





}
