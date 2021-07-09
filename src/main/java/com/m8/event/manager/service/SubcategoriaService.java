package com.m8.event.manager.service;

import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.entity.Subcategoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.SubcategoriaRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;


public class SubcategoriaService {
    
    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    
   @Transactional
    public void crearSubcategoria(String nombre, Categoria categoria, String descripcion) 
            throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("No se ha ingresado ningun nombre válido.");
        }
        
        if (categoria == null) {
            throw new ErrorServicio("Debe asignar una categoría de evento.");
        }
        
        Subcategoria subcategoria = new Subcategoria();

        subcategoria.setNombre(nombre);
        subcategoria.setCategoria(categoria);
        subcategoria.setDescripcion(descripcion);

        subcategoriaRepository.save(subcategoria);
    }  
    
//    @Transactional
//    public void modificarSubcategoria(String nombre) throws ErrorServicio {
//
//        List<Subcategoria> subcategorias = subcategoriaRepository.buscarPorNombre(nombre);
//
//        if (respuesta.isPresent()) {
//            Subcategoria subcategoria = respuesta.get();
//
//            subcategoria.setNombre(nombre);
//
//            subcategoriaRepository.save(subcategoria);
//
//        } else {
//            throw new ErrorServicio("No se encontró esa categoría en la base de datos.");
//        }
//
//    }
    
    
}
