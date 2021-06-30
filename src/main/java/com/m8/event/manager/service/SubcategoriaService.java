package com.m8.event.manager.service;

import com.m8.event.manager.entity.Subcategoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.SubcategoriaRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;


public class SubcategoriaService {
    
    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    
   @Transactional
    public void crearSubcategoria(String nombre,String descripcion) throws ErrorServicio {

        if (nombre == null) {
            throw new ErrorServicio("No se ha ingresado ningun nombre válido");
        }

        Subcategoria subcategoria = new Subcategoria();

        subcategoria.setNombre(nombre);

        subcategoriaRepository.save(subcategoria);
    }  
    
    @Transactional
    public void modificarSubcategoria(String nombre) throws ErrorServicio {

        Optional<Subcategoria> respuesta = subcategoriaRepository.buscarPorNombre(nombre);

        if (respuesta.isPresent()) {
            Subcategoria subcategoria = respuesta.get();

            subcategoria.setNombre(nombre);

            subcategoriaRepository.save(subcategoria);

        } else {
            throw new ErrorServicio("No se encontró esa categoría en la base de datos.");
        }

    }
}


