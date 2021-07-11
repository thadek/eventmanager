package com.m8.event.manager.rest;
import com.m8.event.manager.entity.Categoria;
import com.m8.event.manager.entity.Subcategoria;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.SubcategoriaRepository;
import com.m8.event.manager.service.SubcategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/subcategorias")
public class subcategoriaRestController {

    @Autowired
    private SubcategoriaService ss;

    @Autowired
    private SubcategoriaRepository sr;


    @GetMapping("/ver")
    public List<Subcategoria> verSubcategorias(){
        return  sr.findAll();
    }


    @GetMapping("/ver/{idSubcategoria}")
    public Subcategoria verSubcategoria(@PathVariable("idSubcategoria") Integer idSubcategoria){
        return sr.findById(idSubcategoria).get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/crear")
    public HashMap crearSubcategoria(@RequestBody Subcategoria nuevaSubcategoria)  {
        HashMap<String, String> respuesta = new HashMap<>();
        try{
            Subcategoria subcategoria =
                  ss.crearSubcategoria (
                        nuevaSubcategoria.getNombre (),
                        nuevaSubcategoria.getCategoria (),
                        nuevaSubcategoria.getDescripcion ());

            respuesta.put("respuesta","Subcategoría creada exitosamente.");
            respuesta.put("idSubcategoria", subcategoria.getIdSubcategoria ().toString());
            respuesta.put("nombreSubcategoria", subcategoria.getNombre ());
//            respuesta.put("nombreCategoria", subcategoria.getCategoria ().toString ());

            return respuesta;
        }catch(ErrorServicio e){
            respuesta.put("respuesta","Ocurrió un error: " + e.getMessage());
            return respuesta;
        }
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/modificar")
    public HashMap modificarSubCategoria(@RequestBody Subcategoria subcategoria)  {
        HashMap<String, String> map = new HashMap<>();
        try{
            ss.modificarSubcategoria (
                  subcategoria.getIdSubcategoria (),
                  subcategoria.getNombre(),
                  subcategoria.getCategoria (),
                  subcategoria.getDescripcion ());
            map.put("respuesta","Subcategoria modificada correctamente.");

            return map;
        }catch(ErrorServicio e){
            map.put("respuesta","Error al modificar: " + e.getMessage());
            return map;
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/eliminar")
    public HashMap eliminarSubcategoria(@RequestBody Subcategoria subcategoria) {
        HashMap<String, String> map = new HashMap<>();
        try{
            sr.delete (subcategoria);
            map.put("respuesta","Subategoría eliminada correctamente.");
            return map;
        }catch(Exception e){
            map.put("respuesta","Error al eliminar, ¿Esta subcategoría está asignada a una categoria?");
            return map;
        }
    }

}
