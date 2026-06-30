package com.utp.yachaytinkiy.application.mapper;

import com.utp.yachaytinkiy.domain.model.*;
import com.utp.yachaytinkiy.application.dto.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InscripcionMapper {

    public InscripcionDTO toDTO(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        return new InscripcionDTO(
                inscripcion.getId(),
                inscripcion.getAlumno().getId(),
                inscripcion.getAlumno().getNombreCompleto(),
                inscripcion.getCurso().getId(),
                inscripcion.getCurso().getTitulo(),
                inscripcion.getProgreso(),
                inscripcion.getFechaInscripcion(),
                inscripcion.getCantidadLeccionesCompletadas(),
                inscripcion.getCurso().getCantidadLecciones(),
                inscripcion.estaCompleto()
        );
    }
    public InscripcionResponseDTO toResponseDTO(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        // Alumno básico
        AlumnoBasicoDTO alumnoDTO = new AlumnoBasicoDTO(
                inscripcion.getAlumno().getId(),
                inscripcion.getAlumno().getNombreCompleto(),
                inscripcion.getAlumno().getEmail()
        );

        // Curso básico (sin referencias circulares)
        CursoBasicoDTO cursoDTO = new CursoBasicoDTO(
                inscripcion.getCurso().getId(),
                inscripcion.getCurso().getTitulo(),
                inscripcion.getCurso().getCategoria(),
                inscripcion.getCurso().getNivel().name()
        );

        return new InscripcionResponseDTO(
                inscripcion.getId(),
                alumnoDTO,
                cursoDTO,
                inscripcion.getFechaInscripcion(),
                inscripcion.getProgreso()
        );
    }
    public ProgresoDTO toProgresoDTO(Inscripcion inscripcion) {
        if (inscripcion == null) {
            return null;
        }

        ProgresoDTO dto = new ProgresoDTO();
        dto.setInscripcionId(inscripcion.getId());
        dto.setTituloCurso(inscripcion.getCurso().getTitulo());
        dto.setPorcentajeProgreso(inscripcion.getProgreso());
        dto.setLeccionesCompletadas(inscripcion.getCantidadLeccionesCompletadas());
        dto.setTotalLecciones(inscripcion.getCurso().getCantidadLecciones());
        dto.setCompletado(inscripcion.estaCompleto());

        // Mapear módulos con progreso
        List<ModuloProgresoDTO> modulos = inscripcion.getCurso().getModulos().stream()
                .map(modulo -> toModuloProgresoDTO(modulo, inscripcion))
                .collect(Collectors.toList());
        dto.setModulos(modulos);

        return dto;
    }

    private ModuloProgresoDTO toModuloProgresoDTO(Modulo modulo, Inscripcion inscripcion) {
        List<LeccionProgresoDTO> lecciones = modulo.getLecciones().stream()
                .map(leccion -> toLeccionProgresoDTO(leccion, inscripcion))
                .collect(Collectors.toList());

        return new ModuloProgresoDTO(
                modulo.getId(),
                modulo.getNombre(),
                modulo.getOrden(),
                lecciones
        );
    }

    private LeccionProgresoDTO toLeccionProgresoDTO(Leccion leccion, Inscripcion inscripcion) {
        boolean completada = inscripcion.esLeccionCompletada(leccion);

        // Buscar la fecha de completado si está completada
        LocalDateTime fechaCompletado = null;
        if (completada) {
            fechaCompletado = inscripcion.getLeccionesCompletadas().stream()
                    .filter(pl -> pl.getLeccion().equals(leccion))
                    .findFirst()
                    .map(ProgresoLeccion::getFechaCompletado)
                    .orElse(null);
        }

        return new LeccionProgresoDTO(
                leccion.getId(),
                leccion.getTitulo(),
                leccion.getTipoContenido().name(),
                leccion.getOrden(),
                completada,
                fechaCompletado,
                leccion.getUrlContenido(), 
                leccion.getDescripcion()
        );
    }
}
