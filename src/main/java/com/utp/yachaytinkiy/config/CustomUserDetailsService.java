package com.utp.yachaytinkiy.config;

import com.utp.yachaytinkiy.domain.model.Alumno;
import com.utp.yachaytinkiy.domain.model.Docente;
import com.utp.yachaytinkiy.domain.model.Usuario;
import com.utp.yachaytinkiy.domain.repository.AlumnoRepository;
import com.utp.yachaytinkiy.domain.repository.DocenteRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

/**
 * Servicio de autenticación personalizado para Spring Security.
 *
 * Implementa UserDetailsService para permitir que Spring Security: - Busque
 * usuarios por email en la base de datos - Cargue los roles y permisos del
 * usuario - Valide credenciales durante el login
 *
 * Este servicio busca usuarios en dos tablas: - alumnos (rol: ALUMNO) -
 * docentes (rol: DOCENTE)
 *
 * @author YachayTinkiy Team
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AlumnoRepository alumnoRepository;
    private final DocenteRepository docenteRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param alumnoRepository Repositorio de alumnos
     * @param docenteRepository Repositorio de docentes
     */
    public CustomUserDetailsService(AlumnoRepository alumnoRepository,
            DocenteRepository docenteRepository) {
        this.alumnoRepository = alumnoRepository;
        this.docenteRepository = docenteRepository;
    }

    /**
     * Carga un usuario por su nombre de usuario (email).
     *
     * Este método es llamado automáticamente por Spring Security durante el
     * login.
     *
     * Flujo de ejecución: 1. Busca el usuario primero como Alumno 2. Si no lo
     * encuentra, busca como Docente 3. Si no lo encuentra en ninguna tabla,
     * lanza UsernameNotFoundException 4. Si lo encuentra, construye un objeto
     * UserDetails con: - Email (username) - Contraseña encriptada - Rol
     * (ROLE_ALUMNO o ROLE_DOCENTE)
     *
     * @param email El email del usuario (usado como username)
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar primero como Alumno
        Optional<Alumno> alumnoOpt = alumnoRepository.findByEmail(email);
        if (alumnoOpt.isPresent()) {
            Alumno alumno = alumnoOpt.get();
            return buildUserDetails(alumno);
        }

        // Si no es alumno, buscar como Docente
        Optional<Docente> docenteOpt = docenteRepository.findByEmail(email);
        if (docenteOpt.isPresent()) {
            Docente docente = docenteOpt.get();
            return buildUserDetails(docente);
        }

        // Usuario no encontrado
        throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
    }

    /**
     * Construye un objeto UserDetails a partir de un Usuario.
     *
     * UserDetails es la interfaz de Spring Security que contiene: - Username
     * (en nuestro caso, el email) - Password (contraseña encriptada) -
     * Authorities (roles y permisos) - Flags de cuenta (habilitada, bloqueada,
     * expirada, etc.)
     *
     * @param usuario Usuario del dominio (Alumno o Docente)
     * @return UserDetails para Spring Security
     */
    private UserDetails buildUserDetails(Usuario usuario) {
        // Construir el rol con el prefijo "ROLE_"
        // Spring Security requiere que los roles tengan este prefijo
        String rol = "ROLE_" + usuario.getRol().name();

        // Crear una autoridad (rol) para el usuario
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rol);

        // Construir y retornar el UserDetails
        return User.builder()
                .username(usuario.getEmail()) // Email como username
                .password(usuario.getPassword()) // Contraseña encriptada
                .authorities(Collections.singletonList(authority)) // Lista de roles
                .accountExpired(false) // Cuenta no expirada
                .accountLocked(false) // Cuenta no bloqueada
                .credentialsExpired(false) // Credenciales no expiradas
                .disabled(false) // Cuenta habilitada
                .build();
    }
}
