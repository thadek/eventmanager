package com.m8.event.manager.service;

import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.entity.Evento;
import com.m8.event.manager.entity.Subcategoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.EventoRepository;
import com.m8.event.manager.repository.SubcategoriaRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubcategoriaService {

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Autowired
    private EventoRepository eventoRepository;


    /* Modifico return de crear y modificar subcategoría*/
    @Transactional
    public Subcategoria crearSubcategoria(String nombre, Categoria categoria, String descripcion)
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
        return subcategoria;
    }

    @Transactional
    public Subcategoria modificarSubcategoria(Integer idSubcategoria, String nombre,
            Categoria categoria, String descripcion) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Debe ingresar un nombre válido.");
        }

        if (categoria == null) {
            throw new ErrorServicio("Debe asignar una categoría de evento.");
        }

        Optional<Subcategoria> respuesta = subcategoriaRepository.findById(idSubcategoria);

        if (respuesta.isPresent()) {
            Subcategoria subcategoria = respuesta.get();

            subcategoria.setNombre(nombre);
            subcategoria.setCategoria(categoria);
            subcategoria.setDescripcion(descripcion);

            subcategoriaRepository.save(subcategoria);
            return subcategoria;

        } else {
            throw new ErrorServicio("No se encontró esa categoría en la base de datos.");
        }

    }

    @Transactional
    public void eliminarSubcategoria(Integer idSubcategoria) throws ErrorServicio {

        Optional<Subcategoria> respuesta = subcategoriaRepository.findById(idSubcategoria);
        if (!respuesta.isPresent()) {
            throw new ErrorServicio("No hay ninguna subcategoría con ese nombre");
        }

        List<Evento> eventos = eventoRepository.buscarPorSubcategoria(idSubcategoria);

        if (eventos.isEmpty()) {
            subcategoriaRepository.deleteById(idSubcategoria);
        } else {
            throw new ErrorServicio("No se puede eliminar una subcategoria "
                    + "que esté asignada a un evento.");
        }
    }
}
