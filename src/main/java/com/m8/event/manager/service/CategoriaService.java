package com.m8.event.manager.service;

import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.CategoriaRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

   /* @Tana: Adapté el service de Rol para manejarlo por REST
    Cambio retorno de void a Categoria
    */
   @Transactional
    public Categoria crearCategoria(String nombre) throws ErrorServicio {

        if (nombre == null) {
            throw new ErrorServicio("No se ha ingresado ningun nombre válido");
        }

        Categoria categoria = new Categoria();

        categoria.setNombre(nombre);

        return categoriaRepository.save(categoria);
    }  


       /* @Tana: Adapté el service de Rol para manejarlo por REST
         Cambio retorno de void a Categoria
    */

    @Transactional
    public Categoria modificarCategoria(String nombre, Integer idCategoria) throws ErrorServicio {

        Categoria categoria = categoriaRepository.buscarPorCategoria(nombre);

        if (categoria != null) {
            
            categoria.setNombre(nombre);

            categoriaRepository.save(categoria);

            return categoria;

        } else {
            throw new ErrorServicio("No se encontró esa categoría en la base de datos.");
        }

    }
    
}