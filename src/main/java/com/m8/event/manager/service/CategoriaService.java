package com.m8.event.manager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.CategoriaRepository;

import java.util.Optional;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

   @Autowired
   private CategoriaRepository categoriaRepository;

   /* @Tana: Adapté el service de Rol para manejarlo por REST
    Cambio retorno de void a Categoria
    */
   @Transactional
   public Categoria crearCategoria (String nombre) throws ErrorServicio {

      if (nombre == null || nombre.isEmpty ()) {
         throw new ErrorServicio ("No se ha ingresado ningun nombre válido");
      }

      Categoria categoria = new Categoria ();

      categoria.setNombre (nombre);
//        AGREGAR SUBCATEGORIAS???

      categoriaRepository.save (categoria);
      return categoria;
   }


       /* @Tana: Adapté el service de Rol para manejarlo por REST
         Cambio retorno de void a Categoria  */

   @Transactional
   public Categoria modificarCategoria (String nombre, Integer idCategoria) throws ErrorServicio {

      Optional<Categoria> respuesta = categoriaRepository.findById (idCategoria);

      if (respuesta.isPresent ()) {
         Categoria categoria = respuesta.get ();
         categoria.setNombre (nombre);
         return categoriaRepository.save (categoria);
      } else {
         return null;
      }

   }

   @Transactional
   public void eliminarCategoria (Integer idCategoria) throws ErrorServicio {

      Optional<Categoria> respuesta = categoriaRepository.findById (idCategoria);
      if (respuesta.isPresent ()) {

         Categoria categoria = respuesta.get ();

         if (categoria.getSubcategorias ().isEmpty ()) {
            categoriaRepository.deleteById (idCategoria);
         } else {
            throw new ErrorServicio ("No se puede eliminar una categoria " + "con subcategorías relacionadas.");
         }

      } else {
         throw new ErrorServicio ("No hay ninguna categoría con ese nombre");
      }
   }

}