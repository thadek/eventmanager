package com.m8.event.manager.service;

import com.m8.event.manager.entity.Rol;
import com.m8.event.manager.entity.Usuario;
import com.m8.event.manager.error.ErrorServicio;
import com.m8.event.manager.repository.RolRepository;
import com.m8.event.manager.repository.UsuarioRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RolService rolService;

    @Transactional
    public Usuario crearUsuario(String username, String password, String password2,
            Integer idRol) throws ErrorServicio {

        boolean crear = true;

        validarDatos(crear, username, password, password2);

        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(encoder.encode(password));

        if (idRol == null) {
            usuario.setRol(rolRepository.buscarPorRol("ALUMNO"));
        } else {

            if (rolRepository.findById(idRol).isPresent()) {
                Rol rol = rolRepository.findById(idRol).get();
                usuario.setRol(rol);
            } else {
                throw new ErrorServicio("No existe ningún rol con ese id");
            }
        }

        return usuarioRepository.save(usuario);

    }

    @Transactional
    public void modificarUsuario(String username, String password, String password2, 
            Integer idRol) throws ErrorServicio {
        
        boolean crear = false;

        validarDatos(crear, username, password, password2);

        Optional<Usuario> respuesta = usuarioRepository.findById(username);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setUsername(username);
            System.out.println( password);
            usuario.setPassword(encoder.encode(password));

            if (idRol == null) {
                usuario.setRol((Rol) rolRepository.buscarPorRol("ALUMNO"));
            } else {

                if (rolRepository.findById(idRol).isPresent()) {
                    Rol rol = rolRepository.findById(idRol).get();
                    usuario.setRol(rol);
                } else {
                    throw new ErrorServicio("No existe ningún rol con ese id");
                }
            }

            usuarioRepository.save(usuario);

        } else {
            throw new ErrorServicio("No se encontró ese usuario en la base de datos.");
        }

    }


    private void validarDatos(boolean crear, String username, String password, String password2) throws ErrorServicio {

        if (username == null || username.isEmpty()) {
            throw new ErrorServicio("Debe ingresar su username");
        }
        
        if (crear) {

            Optional<Usuario> respuesta = usuarioRepository.findById(username);

            if (respuesta.isPresent()) {

                throw new ErrorServicio("Ya existe una cuenta con ese nombre de usuario. Por favor elija uno distinto");
            }
        }        

        if (password == null || password.isEmpty() || password.length() < 6) {
            throw new ErrorServicio("La contraseña no puede ser nula ni tener menos "
                    + "de 6 caracteres.");
        }

        if (password2 == null || password2.isEmpty() || password2.length() < 6) {
            throw new ErrorServicio("La contraseña no puede ser nula ni tener menos "
                    + "de 6 caracteres.");
        }

        if (!password.equals(password2)) {
            throw new ErrorServicio("Las contraseñas deben ser iguales.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("No hay ningún usuario con username " + username);
        }

        GrantedAuthority rol = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombreRol());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        session.setAttribute("usuarioSession", usuario);
//        session.setAttribute("usuarioEmail", usuario.getDocumento());

        return new User(usuario.getUsername(), usuario.getPassword(), Collections.singletonList(rol));
    }

}
