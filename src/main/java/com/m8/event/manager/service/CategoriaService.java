
package com.m8.event.manager.service;

import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.CategoriaRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    
   @Transactional
    public void crearCategoria(String nombre) throws ErrorServicio {

        if (nombre == null) {
            throw new ErrorServicio("No se ha ingresado ningun nombre válido");
        }

        Categoria categoria = new Categoria();

        categoria.setNombre(nombre);

        categoriaRepository.save(categoria);
    }  
    
    @Transactional
    public void modificarCategoria(String nombre) throws ErrorServicio {

        Optional<Categoria> respuesta = categoriaRepository.buscarPorNombre(nombre);

        if (respuesta.isPresent()) {
            Categoria categoria = respuesta.get();

            categoria.setNombre(nombre);

            categoriaRepository.save(categoria);

        } else {
            throw new ErrorServicio("No se encontró esa categoría en la base de datos.");
        }

    }
}

