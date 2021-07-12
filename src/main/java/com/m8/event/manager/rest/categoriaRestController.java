package com.m8.event.manager.rest;
import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.CategoriaRepository;
import com.m8.event.manager.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class categoriaRestController {

    @Autowired
    private CategoriaService cs;

    @Autowired
    private CategoriaRepository cr;


    @GetMapping("/ver")
    public List<Categoria> verCategorias1()  {
      return cr.findAll();
    }


    @GetMapping("/ver/{idCategoria}")
    public Categoria verCategoria(@PathVariable("idCategoria") Integer idCategoria){
        return cr.findById(idCategoria).get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/crear")
    public HashMap crearCategoria(@RequestBody Categoria nuevaCategoria)  {
        HashMap<String, String> respuesta = new HashMap<>();
        try{
            Categoria categoria = cs.crearCategoria(nuevaCategoria.getNombre ());
            respuesta.put("respuesta","Categoría creada exitosamente.");
            respuesta.put("idCategoria", categoria.getIdCategoria ().toString());
            respuesta.put("nombreCategoria", categoria.getNombre ());
            //@tana
            //AGREGAR LISTADO DE SUBCATEGORIAS -> DEBO RECIBIRLA POR PARÁMETRO?
            //HAY QUE INICIALIZAR LA LISTA DE SUBCATEGORÍAS? PORQUE PODRÍA SER NULA
            return respuesta;
        }catch(ErrorServicio e){
            respuesta.put("respuesta","Ocurrió un error: " + e.getMessage());
            return respuesta;
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modificar")
    public HashMap modificarCategoria(@RequestBody Categoria categoria)  {
        HashMap<String, String> map = new HashMap<>();
        try{
            cs.modificarCategoria (categoria.getNombre (),categoria.getIdCategoria ());
            map.put("respuesta","Categoria modificada correctamente.");

            //@tana
            //Deberíamos agregar como posibilidad de modificación agregar/quitar subcategorías ?

            return map;
        }catch(ErrorServicio e){
            map.put("respuesta","Error al modificar: " + e.getMessage());
            return map;
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/eliminar")
    public HashMap eliminarCategoria(@RequestBody Categoria categoria) {
        HashMap<String, String> map = new HashMap<>();
        try{
            cr.delete (categoria);
            map.put("respuesta","Categoría eliminada correctamente.");
            return map;
        }catch(Exception e){
            map.put("respuesta","Error al eliminar, ¿Esta categoría tiene subcategorías asignadas?");
            return map;
        }
    }

}
