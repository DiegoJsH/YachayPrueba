package com.utp.yachaytinkiy.application.service;

import com.utp.yachaytinkiy.domain.model.*;
import com.utp.yachaytinkiy.domain.repository.*;
import com.utp.yachaytinkiy.application.dto.InscripcionDTO;
import com.utp.yachaytinkiy.application.dto.InscripcionResponseDTO;
import com.utp.yachaytinkiy.application.dto.ProgresoDTO;
import com.utp.yachaytinkiy.application.mapper.InscripcionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final AlumnoRepository alumnoRepository;
    private final CursoRepository cursoRepository;
    private final LeccionRepository leccionRepository;
    private final InscripcionMapper inscripcionMapper;    
    private final CertificadoService certificadoService;

    public InscripcionService(InscripcionRepository inscripcionRepository,
            AlumnoRepository alumnoRepository,
            CursoRepository cursoRepository,
            LeccionRepository leccionRepository,
            InscripcionMapper inscripcionMapper,
            CertificadoService certificadoService) {
        this.inscripcionRepository = inscripcionRepository;
        this.alumnoRepository = alumnoRepository;
        this.cursoRepository = cursoRepository;
        this.leccionRepository = leccionRepository;
        this.inscripcionMapper = inscripcionMapper;
        this.certificadoService = certificadoService;
    }

    @Transactional
    public InscripcionResponseDTO inscribirAlumno(Long alumnoId, Long cursoId) {
        // Validar que el alumno exista
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Alumno no encontrado con ID: " + alumnoId
        ));

        // Validar que el curso exista
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Curso no encontrado con ID: " + cursoId
        ));

        // Verificar que no esté ya inscrito
        if (inscripcionRepository.existsByAlumnoIdAndCursoId(alumnoId, cursoId)) {
            throw new IllegalStateException(
                    "El alumno ya esta inscrito en este curso"
            );
        }

        // Crear la inscripción
        Inscripcion inscripcion = new Inscripcion(alumno, curso);
        Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);

        // Retornar DTO en lugar de entidad
        return inscripcionMapper.toResponseDTO(inscripcionGuardada);
    }

    @Transactional
    public InscripcionResponseDTO marcarLeccionCompletada(Long inscripcionId, Long leccionId) {
        // Buscar la inscripción
        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Inscripcion no encontrada con ID: " + inscripcionId
        ));

        // Buscar la lección
        Leccion leccion = leccionRepository.findById(leccionId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Leccion no encontrada con ID: " + leccionId
        ));

        // Marcar como completada
        inscripcion.marcarLeccionCompletada(leccion);

        // Actualizar el progreso
        inscripcion.actualizarProgreso();
        Inscripcion inscripcionActualizada = inscripcionRepository.save(inscripcion);

        // GENERAR CERTIFICADO SI COMPLETÓ EL CURSO (100%)
        if (inscripcionActualizada.estaCompleto()) {
            System.out.println("Curso completado al 100%, generando certificado...");
            try {
                certificadoService.generarCertificado(
                        inscripcionActualizada.getAlumno().getId(),
                        inscripcionActualizada.getCurso().getId()
                );
                System.out.println("Certificado generado automáticamente");
            } catch (Exception e) {
                System.err.println("Error al generar certificado: " + e.getMessage());
                // No lanzar excepción, solo log (certificado se puede generar después)
            }
        }
        return inscripcionMapper.toResponseDTO(inscripcionActualizada);
    }

    @Transactional(readOnly = true)
    public ProgresoDTO obtenerProgreso(Long inscripcionId) {
        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new IllegalArgumentException(
                "Inscripcion no encontrada con ID: " + inscripcionId
        ));

        return inscripcionMapper.toProgresoDTO(inscripcion);
    }

    @Transactional(readOnly = true)
    public List<InscripcionDTO> listarInscripcionesPorAlumno(Long alumnoId) {
        return inscripcionRepository.findByAlumnoId(alumnoId).stream()
                .map(inscripcionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InscripcionDTO> listarInscripcionesPorCurso(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId).stream()
                .map(inscripcionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InscripcionDTO obtenerInscripcion(Long alumnoId, Long cursoId) {
        Inscripcion inscripcion = inscripcionRepository
                .findByAlumnoIdAndCursoId(alumnoId, cursoId)
                .orElseThrow(() -> new IllegalArgumentException(
                "No se encontro inscripcion para el alumno " + alumnoId
                + " en el curso " + cursoId
        ));

        return inscripcionMapper.toDTO(inscripcion);
    }
}
