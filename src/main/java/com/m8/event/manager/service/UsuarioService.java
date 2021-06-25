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
    public void crearUsuario(String username, String password, Integer idRol) throws ErrorServicio {

        validarDatos(username, password);

        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(encoder.encode(password));

        if (idRol == null) {
            usuario.setRol(rolRepository.findByRol("ALUMNO"));
        } else {

            if (rolRepository.findById(idRol).isPresent()) {
                Rol rol = rolRepository.findById(idRol).get();
                usuario.setRol(rol);
            } else {
                throw new ErrorServicio("No existe ningún rol con ese id");
            }
        }

        usuarioRepository.save(usuario);

    }

    @Transactional
    public void modificarUsuario(String username, String password, Integer idRol) throws ErrorServicio {

        validarDatos(username, password);

        Optional<Usuario> respuesta = usuarioRepository.findById(username);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            usuario.setUsername(username);

            usuario.setPassword(encoder.encode(password));

            if (idRol == null) {
                usuario.setRol(rolRepository.findByRol("ALUMNO"));
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

    @Transactional
    public List<Usuario> verTodosLosUsuarios() throws ErrorServicio {

        return usuarioRepository.findAll();

    }

//    @Transactional
//    public void eliminarUsuarioPorDoc(Long documento) throws ErrorServicio {
//
//        Optional<Usuario> respuesta = ur.findById(documento);
//        if (respuesta.isPresent()) {
//
//            ur.deleteById(documento);
//
//        } else {
//            throw new ErrorServicio("No hay ningún usuario con ese número de documento");
//        }
//
//    }
    
    private void validarDatos(String username, String password) throws ErrorServicio {

        if (username == null || username.isEmpty()) {
            throw new ErrorServicio("Debe ingresar su username");
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            throw new ErrorServicio("La contraseña no puede ser nula ni tener menos "
                    + "de 6 caracteres.");
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
