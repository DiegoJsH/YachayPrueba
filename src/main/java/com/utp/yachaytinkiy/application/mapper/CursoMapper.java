package com.utp.yachaytinkiy.application.mapper;

import com.utp.yachaytinkiy.domain.model.Curso;
import com.utp.yachaytinkiy.application.dto.CursoDTO;
import com.utp.yachaytinkiy.application.dto.CursoResponseDTO;
import com.utp.yachaytinkiy.application.dto.DocenteBasicoDTO;
import com.utp.yachaytinkiy.application.dto.LeccionDTO;
import com.utp.yachaytinkiy.application.dto.ModuloDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper {

    public CursoDTO toDTO(Curso curso) {
        if (curso == null) {
            return null;
        }

        return new CursoDTO(
                curso.getId(),
                curso.getTitulo(),
                curso.getDescripcion(),
                curso.getCategoria(),
                curso.getNivel().name(),
                curso.getDuracion(),
                curso.getPrecio(),
                curso.getDocente().getNombreCompleto(),
                curso.getDocente().getId(),
                curso.getModulos().size(),
                curso.getCantidadLecciones(),
                curso.getFechaCreacion()
        );
    }
    public CursoResponseDTO toResponseDTO(Curso curso) {
        if (curso == null) {
            return null;
        }

        // Crear DocenteBasicoDTO sin referencia circular
        DocenteBasicoDTO docenteDTO = new DocenteBasicoDTO(
                curso.getDocente().getId(),
                curso.getDocente().getNombreCompleto(),
                curso.getDocente().getEspecialidad()
        );

        // Mapear módulos y lecciones
        List<ModuloDTO> modulosDTO = curso.getModulos().stream()
                .map(modulo -> {
                    List<LeccionDTO> leccionesDTO = modulo.getLecciones().stream()
                            .map(leccion -> new LeccionDTO(
                            leccion.getId(),
                            leccion.getTitulo(),
                            leccion.getTipoContenido().name(),
                            leccion.getOrden(),
                            leccion.getUrlContenido(),
                            leccion.getDescripcion()
                    ))
                            .collect(Collectors.toList());

                    return new ModuloDTO(
                            modulo.getId(),
                            modulo.getNombre(),
                            modulo.getOrden(),
                            leccionesDTO
                    );
                })
                .collect(Collectors.toList());

        CursoResponseDTO response = new CursoResponseDTO(
                curso.getId(),
                curso.getTitulo(),
                curso.getDescripcion(),
                curso.getCategoria(),
                curso.getNivel().name(),
                curso.getDuracion(),
                curso.getPrecio(),
                docenteDTO,
                curso.getFechaCreacion()
        );

        // Agregar módulos y stats
        response.setModulos(modulosDTO);
        response.setCantidadModulos(modulosDTO.size());
        response.setCantidadLecciones(curso.getCantidadLecciones());

        return response;
    }
}
