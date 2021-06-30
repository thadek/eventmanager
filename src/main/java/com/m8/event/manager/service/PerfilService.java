package com.m8.event.manager.service;

import com.m8.event.manager.entity.Foto;
import com.m8.event.manager.entity.Perfil;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.PerfilRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Agustin
 */
@Service
public class PerfilService {

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    FotoService fotoService;

    @Transactional
    public void agregarPerfil(String username, MultipartFile archivo, String email, String nombre,
            String apellido, String tel, LocalDate fechaNac, String descripcion) throws ErrorServicio {

        Usuario usuario = usuarioRepository.findByUsername(username);

        validar(nombre);

        Perfil perfil = new Perfil();

        perfil.setEmail(email);
        perfil.setNombre(nombre);
        perfil.setApellido(apellido);
        perfil.setUsuario(usuario);
        perfil.setTel(tel);
        perfil.setFechaNac(fechaNac);
        perfil.setDescripcion(descripcion);

        Foto foto = fotoService.guardar(archivo);
        perfil.setFoto(foto);

        perfilRepository.save(perfil);

    }

    @Transactional
    public void modificar(Integer id, String username, MultipartFile archivo, String email, String nombre,
            String apellido, String tel, LocalDate fechaNac, String descripcion) throws ErrorServicio {

        validar(nombre);

        Optional<Perfil> respuesta = perfilRepository.findById(id);
        if (respuesta.isPresent()) {

            Perfil perfil = respuesta.get();

            perfil.setEmail(email);
            perfil.setNombre(nombre);
            perfil.setApellido(apellido);
            perfil.setTel(tel);
            perfil.setFechaNac(fechaNac);
            perfil.setDescripcion(descripcion);

            String idFoto = null;
            if (perfil.getFoto() != null) {
                idFoto = perfil.getFoto().getId();
            }

            Foto foto = fotoService.actualizar(idFoto, archivo);
            perfil.setFoto(foto);

            perfilRepository.save(perfil);
        } else {

            throw new ErrorServicio("No existe un perfil con el Id solicitado");
        }
    }

    public void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {

            throw new ErrorServicio("El nombre no puede ser nulo");

        }

    }

}
