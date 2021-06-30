package com.m8.event.manager.service;

import com.m8.event.manager.entity.Foto;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.FotoRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/** @author Agustin */


public class FotoService {
    
    @Autowired
    private FotoRepository fotoRepositorio;
    
    @Transactional
    public Foto guardar(MultipartFile archivo) throws ErrorServicio{
    if (archivo != null){
        try{
        
            Foto foto = new Foto();
            foto.setMime(archivo.getContentType());
            foto.setNombre(archivo.getName());
            foto.setContenido(archivo.getBytes());

            return fotoRepositorio.save(foto);
        }catch(Exception e){
        System.err.print(e.getMessage());
        }
    
    }
        
    return null;
          
    
    }
    
    
    @Transactional
    public Foto actualizar(String idFoto, MultipartFile archivo) throws ErrorServicio{
    if (archivo != null){
        try{
        
        Foto foto = new Foto();
        
        
        if(idFoto != null){
            Optional<Foto> respuesta = fotoRepositorio.findById(idFoto);
            if(respuesta.isPresent()){
            foto=respuesta.get();
            }
        }
        
        foto.setMime(archivo.getContentType());
        foto.setMime(archivo.getName());
        foto.setContenido(archivo.getBytes());
        
        return fotoRepositorio.save(foto);
        
        }catch(Exception e){
            System.err.print(e.getMessage());
        }
    
    }
    
    
    return null;
    
           
    
    }

}
