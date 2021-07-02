package com.m8.event.manager.service;

import com.m8.event.manager.entity.Rol;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.RolRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    @Transactional
    public void crearRol(String nombre) throws ErrorServicio {

        validarDatos(nombre);

        Rol rol = new Rol();

        rol.setNombreRol(nombre);

        rolRepository.save(rol);

    }

    @Transactional
    public void modificarRol(String nombre, Integer idRol) throws ErrorServicio {

        validarDatos(nombre);

        Optional<Rol> respuesta = rolRepository.findById(idRol);

        if (respuesta.isPresent()) {
            Rol rol = respuesta.get();

            rol.setNombreRol(nombre);

            rolRepository.save(rol);

        }

    }




    private void validarDatos(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Debe ingresar un nombre para el rol");
        }
    }

}
