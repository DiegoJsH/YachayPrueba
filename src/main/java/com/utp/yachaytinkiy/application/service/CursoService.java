package com.utp.yachaytinkiy.application.service;

import com.utp.yachaytinkiy.domain.model.*;
import com.utp.yachaytinkiy.domain.repository.CursoRepository;
import com.utp.yachaytinkiy.domain.repository.DocenteRepository;
import com.utp.yachaytinkiy.application.dto.*;
import com.utp.yachaytinkiy.application.mapper.CursoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión completa de cursos con módulos y lecciones.
 *
 * @author YachayTinkiy Team
 */
@Service
public class CursoService {

    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final CursoMapper cursoMapper;

    public CursoService(CursoRepository cursoRepository,
            DocenteRepository docenteRepository,
            CursoMapper cursoMapper) {
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.cursoMapper = cursoMapper;
    }

    /**
     * Crea un curso completo con módulos y lecciones.
     *
     * Este método: 1. Valida que el docente exista 2. Crea el curso base 3.
     * Crea los módulos asociados 4. Crea las lecciones de cada módulo
     *
     * @param dto Datos del curso con módulos y lecciones
     * @return DTO del curso creado
     * @throws IllegalArgumentException si el docente no existe
     */
    @Transactional
    public CursoResponseDTO crearCurso(CrearCursoDTO dto) {
        System.out.println("📚 Creando curso: " + dto.getTitulo());

        // 1. Validar que el docente exista
        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new IllegalArgumentException(
                "Docente no encontrado con ID: " + dto.getDocenteId()
        ));

        // 2. Convertir nivel a Enum
        NivelCurso nivel = NivelCurso.valueOf(dto.getNivel().toUpperCase());

        // 3. Crear el curso base
        Curso curso = new Curso(
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getCategoria(),
                nivel,
                docente
        );

        curso.setDuracion(dto.getDuracion());

        if (dto.getPrecio() != null) {
            curso.setPrecio(dto.getPrecio());
        }

        // 4. Agregar módulos y lecciones
        if (dto.getModulos() != null && !dto.getModulos().isEmpty()) {
            for (CrearModuloDTO moduloDTO : dto.getModulos()) {
                System.out.println("  📂 Agregando módulo: " + moduloDTO.getNombre());

                // Crear módulo
                Modulo modulo = curso.agregarModulo(
                        moduloDTO.getNombre(),
                        moduloDTO.getOrden()
                );

                // Agregar lecciones al módulo
                if (moduloDTO.getLecciones() != null && !moduloDTO.getLecciones().isEmpty()) {
                    for (CrearLeccionDTO leccionDTO : moduloDTO.getLecciones()) {
                        System.out.println("    📄 Agregando lección: " + leccionDTO.getTitulo());

                        // Convertir tipo de contenido a Enum
                        TipoContenido tipoContenido = TipoContenido.valueOf(
                                leccionDTO.getTipoContenido().toUpperCase()
                        );

                        // Crear lección
                        Leccion leccion = modulo.agregarLeccion(
                                leccionDTO.getTitulo(),
                                tipoContenido,
                                leccionDTO.getOrden()
                        );

                        // Asignar propiedades opcionales
                        if (leccionDTO.getUrlContenido() != null) {
                            leccion.setUrlContenido(leccionDTO.getUrlContenido());
                        }
                        if (leccionDTO.getDescripcion() != null) {
                            leccion.setDescripcion(leccionDTO.getDescripcion());
                        }
                    }
                }
            }
        }

        // 5. Guardar curso con cascade
        Curso cursoGuardado = cursoRepository.save(curso);

        System.out.println("✅ Curso creado exitosamente con ID: " + cursoGuardado.getId());
        System.out.println("   Módulos: " + cursoGuardado.getModulos().size());
        System.out.println("   Lecciones totales: " + cursoGuardado.getCantidadLecciones());

        // 6. Retornar DTO
        return cursoMapper.toResponseDTO(cursoGuardado);
    }

    /**
     * Obtiene un curso por ID con toda su información.
     */
    @Transactional(readOnly = true)
    public CursoDTO obtenerCursoPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "Curso no encontrado con ID: " + id
        ));

        return cursoMapper.toDTO(curso);
    }
    
    /**
     * Obtiene un curso con toda su información detallada para mostrar en la
     * vista. Incluye módulos, lecciones y datos del docente.
     *
     * @param id ID del curso
     * @return DTO con información completa del curso
     */
    @Transactional(readOnly = true)
    public CursoResponseDTO obtenerCursoDetallado(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "Curso no encontrado con ID: " + id
        ));

        return cursoMapper.toResponseDTO(curso);
    }
    /**
     * Lista todos los cursos disponibles.
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> listarCursos() {
        return cursoRepository.findAll().stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista cursos por docente.
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> listarCursosPorDocente(Long docenteId) {
        return cursoRepository.findByDocenteId(docenteId).stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca cursos por categoría.
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarCursosPorCategoria(String categoria) {
        return cursoRepository.findByCategoria(categoria).stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca cursos por título (búsqueda parcial).
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> buscarCursosPorTitulo(String titulo) {
        return cursoRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(cursoMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza un curso existente.
     *
     * Nota: Esta versión actualiza solo la información básica. Para
     * módulos/lecciones se requiere lógica adicional.
     */
    @Transactional
    public CursoResponseDTO actualizarCurso(Long id, CrearCursoDTO dto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "Curso no encontrado con ID: " + id
        ));

        // Actualizar información básica
        curso.setTitulo(dto.getTitulo());
        curso.setDescripcion(dto.getDescripcion());
        curso.setCategoria(dto.getCategoria());
        curso.setNivel(NivelCurso.valueOf(dto.getNivel().toUpperCase()));
        curso.setDuracion(dto.getDuracion());

        if (dto.getPrecio() != null) {
            curso.setPrecio(dto.getPrecio());
        }

        Curso cursoActualizado = cursoRepository.save(curso);

        return cursoMapper.toResponseDTO(cursoActualizado);
    }

    /**
     * Elimina un curso.
     *
     * IMPORTANTE: Verifica que no haya inscripciones activas.
     */
    @Transactional
    public void eliminarCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "Curso no encontrado con ID: " + id
        ));

        // TODO: Verificar que no haya inscripciones activas
        // if (inscripcionRepository.countByCursoId(id) > 0) {
        //     throw new IllegalStateException("No se puede eliminar un curso con inscripciones");
        // }
        cursoRepository.delete(curso);
    }
}
