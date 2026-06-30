package com.utp.yachaytinkiy.application.service;

import com.utp.yachaytinkiy.domain.model.Alumno;
import com.utp.yachaytinkiy.domain.model.Docente;
import com.utp.yachaytinkiy.domain.repository.AlumnoRepository;
import com.utp.yachaytinkiy.domain.repository.DocenteRepository;
import com.utp.yachaytinkiy.application.dto.RegistroAlumnoDTO;
import com.utp.yachaytinkiy.application.dto.RegistroDocenteDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de registro de usuarios.
 *
 * Responsabilidades: - Registrar nuevos alumnos - Registrar nuevos docentes -
 * Validar que el email no esté duplicado - Encriptar contraseñas antes de
 * guardar en BD
 *
 * @author YachayTinkiy Team
 */
@Service
public class RegistroService {

    private final AlumnoRepository alumnoRepository;
    private final DocenteRepository docenteRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param alumnoRepository Repositorio de alumnos
     * @param docenteRepository Repositorio de docentes
     * @param passwordEncoder Encoder de contraseñas (BCrypt)
     */
    public RegistroService(AlumnoRepository alumnoRepository,
            DocenteRepository docenteRepository,
            PasswordEncoder passwordEncoder) {
        this.alumnoRepository = alumnoRepository;
        this.docenteRepository = docenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registra un nuevo alumno en el sistema.
     *
     * Validaciones: - Email no puede estar duplicado - Contraseña debe cumplir
     * requisitos de seguridad (validado en DTO)
     *
     * Proceso: 1. Valida que el email no exista 2. Encripta la contraseña con
     * BCrypt 3. Crea la entidad Alumno 4. Guarda en la base de datos
     *
     * @param dto Datos del alumno a registrar
     * @return Alumno registrado
     * @throws IllegalArgumentException si el email ya existe
     */
    @Transactional
    public Alumno registrarAlumno(RegistroAlumnoDTO dto) {
        // Validar que el email no exista en alumnos
        if (alumnoRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un alumno con el email: " + dto.getEmail()
            );
        }

        // Validar que el email no exista en docentes
        if (docenteRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un docente con el email: " + dto.getEmail()
            );
        }

        // Encriptar la contraseña con BCrypt
        String passwordEncriptada = passwordEncoder.encode(dto.getPassword());

        // Crear el alumno con contraseña encriptada
        Alumno alumno = new Alumno(
                dto.getEmail(),
                passwordEncriptada, // ← Contraseña encriptada
                dto.getNombreCompleto()
        );

        // Guardar y retornar
        return alumnoRepository.save(alumno);
    }

    /**
     * Registra un nuevo docente en el sistema.
     *
     * Validaciones: - Email no puede estar duplicado - Contraseña debe cumplir
     * requisitos de seguridad (validado en DTO) - Especialidad y grado
     * académico son obligatorios
     *
     * Proceso: 1. Valida que el email no exista 2. Encripta la contraseña con
     * BCrypt 3. Crea la entidad Docente 4. Guarda en la base de datos
     *
     * @param dto Datos del docente a registrar
     * @return Docente registrado
     * @throws IllegalArgumentException si el email ya existe
     */
    @Transactional
    public Docente registrarDocente(RegistroDocenteDTO dto) {
        // Validar que el email no exista en docentes
        if (docenteRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un docente con el email: " + dto.getEmail()
            );
        }

        // Validar que el email no exista en alumnos
        if (alumnoRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un alumno con el email: " + dto.getEmail()
            );
        }

        // Encriptar la contraseña con BCrypt
        String passwordEncriptada = passwordEncoder.encode(dto.getPassword());

        // Crear el docente con contraseña encriptada
        Docente docente = new Docente(
                dto.getEmail(),
                passwordEncriptada, // ← Contraseña encriptada
                dto.getNombreCompleto(),
                dto.getEspecialidad(),
                dto.getGradoAcademico(),
                dto.getBiografia()
        );

        // Guardar y retornar
        return docenteRepository.save(docente);
    }
    /**
     * Actualiza el perfil de un alumno (solo nombre).
     * @param alumnoId
     * @param nombreCompleto
     * @return Alumno
     */
    @Transactional
    public Alumno actualizarPerfilAlumno(Long alumnoId, String nombreCompleto) {
        // Buscar alumno
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Alumno no encontrado con ID: " + alumnoId
        ));

        // Validar nombre
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre completo no puede estar vacío");
        }

        if (nombreCompleto.length() < 3 || nombreCompleto.length() > 100) {
            throw new IllegalArgumentException(
                    "Nombre debe tener entre 3 y 100 caracteres"
            );
        }

        // Actualizar
        alumno.setNombreCompleto(nombreCompleto.trim());

        // Guardar
        return alumnoRepository.save(alumno);
    }

    /**
     * Actualiza el perfil de un docente.
     * @param docenteId
     * @param nombreCompleto
     * @param especialidad
     * @param biografia
     * @return Docente
     */
    @Transactional
    public Docente actualizarPerfilDocente(Long docenteId, String nombreCompleto,
            String especialidad, String biografia) {
        // Buscar docente
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Docente no encontrado con ID: " + docenteId
        ));

        // Validar nombre
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre completo no puede estar vacío");
        }

        if (nombreCompleto.length() < 3 || nombreCompleto.length() > 100) {
            throw new IllegalArgumentException(
                    "Nombre debe tener entre 3 y 100 caracteres"
            );
        }

        // Validar especialidad
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidad no puede estar vacía");
        }

        if (especialidad.length() < 3 || especialidad.length() > 100) {
            throw new IllegalArgumentException(
                    "Especialidad debe tener entre 3 y 100 caracteres"
            );
        }

        // Validar biografía (opcional)
        if (biografia != null && biografia.length() > 300) {
            throw new IllegalArgumentException(
                    "Biografía no debe exceder 300 caracteres"
            );
        }

        // Actualizar
        docente.setNombreCompleto(nombreCompleto.trim());
        docente.setEspecialidad(especialidad.trim());

        if (biografia != null && !biografia.trim().isEmpty()) {
            docente.setBiografia(biografia.trim());
        }

        // Guardar
        return docenteRepository.save(docente);
    }
}
