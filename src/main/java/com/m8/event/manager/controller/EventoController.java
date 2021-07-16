package com.m8.event.manager.controller;

import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService es;

    @Autowired
    private EventoRepository rp;

    @GetMapping()
    public ModelAndView eventos() {
        ModelAndView mav = new ModelAndView("eventos");
        mav.addObject("title", "Lista de eventos");
        return mav;
    }

    @GetMapping("/ver/{id}")
    public ModelAndView verEvento(@PathVariable("id") Integer id) {

        ModelAndView mav = new ModelAndView("evento-detalle");
        Evento e = rp.findById(id).get();
        mav.addObject("title", e.getNombre() + " - EventManager");
        mav.addObject("evento", e);
        return mav;

    }

}
