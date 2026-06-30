package com.utp.yachaytinkiy.presentation.view;

import com.utp.yachaytinkiy.application.dto.CrearCursoDTO;
import com.utp.yachaytinkiy.application.dto.CursoDTO;
import com.utp.yachaytinkiy.application.dto.CursoResponseDTO;
import com.utp.yachaytinkiy.application.service.CursoService;
import com.utp.yachaytinkiy.domain.repository.DocenteRepository;
import com.utp.yachaytinkiy.domain.model.Docente;
import org.springframework.security.access.AccessDeniedException;
import jakarta.validation.Valid;
import java.util.ArrayList;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de gestión de cursos. Maneja la creación, edición y visualización
 * de cursos para docentes.
 */
@Controller
public class CursoViewController {

    private final CursoService cursoService;
    private final DocenteRepository docenteRepository;

    public CursoViewController(CursoService cursoService,
            DocenteRepository docenteRepository) {
        this.cursoService = cursoService;
        this.docenteRepository = docenteRepository;
    }

    /**
     * Muestra el catálogo de cursos.
     *
     * @param categoria Filtro opcional por categoría
     * @param busqueda Término de búsqueda opcional
     * @param nivel Filtro opcional por niveles
     * @param model Modelo para la vista
     * @return Vista del catálogo
     */
    @GetMapping("/catalogo")
    public String catalogo(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) List<String> nivel, // Niveles
            Model model) {

        List<CursoDTO> cursos;

        if (busqueda != null && !busqueda.isEmpty()) {
            cursos = cursoService.buscarCursosPorTitulo(busqueda);
        } else if (categoria != null && !categoria.isEmpty()) {
            cursos = cursoService.buscarCursosPorCategoria(categoria);
        } else {
            cursos = cursoService.listarCursos();
        }
        //FILTRADO POR NIVEL
        if (nivel != null && !nivel.isEmpty()) {
            cursos = cursos.stream()
                    .filter(curso -> nivel.contains(curso.getNivel()))
                    .collect(Collectors.toList());
        }
        model.addAttribute("cursos", cursos);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("busqueda", busqueda);
        model.addAttribute("nivelesSeleccionados", nivel != null ? nivel : new ArrayList<>());  //AGREGAR al model
        return "catalogo";
    }

    /**
     * Muestra detalle de un curso específico.
     *
     * @param id ID del curso
     * @param model Modelo para la vista
     * @return Vista de detalle del curso
     */
    @GetMapping("/cursos/{id}")
    public String detalleCurso(@PathVariable Long id, Model model) {
        CursoResponseDTO curso = cursoService.obtenerCursoDetallado(id);
        model.addAttribute("curso", curso);
        return "curso-detalle";
    }

    /**
     * Muestra el formulario para crear un nuevo curso.
     *
     * @param model Modelo para la vista
     * @param authentication Información del usuario autenticado
     * @return Vista del formulario de creación
     */
    @GetMapping("/cursos/crear")
    public String crearCursoForm(Model model, Authentication authentication) {
        // Obtener email del docente autenticado
        String email = authentication.getName();

        // Buscar docente
        Docente docente = docenteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));

        CrearCursoDTO dto = new CrearCursoDTO();
        dto.setDocenteId(docente.getId());

        model.addAttribute("crearCursoDTO", dto);
        return "crear-curso";
    }

    /**
     * Procesa la creación de un curso con módulos y lecciones.
     *
     * Incluye REGISTRO detallado de errores de validación para debugging.
     *
     * @param dto Datos del curso a crear
     * @param result Resultado de la validación
     * @param authentication Información del usuario autenticado
     * @param redirectAttributes Atributos para el redirect
     * @return Redirect a la vista correspondiente
     */
    @PostMapping("/cursos/crear")
    public String crearCurso(
            @Valid @ModelAttribute CrearCursoDTO dto,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // ===== REGISTRO DETALLADO DE ERRORES =====
        if (result.hasErrors()) {
            System.out.println("\n🔴 ===== ERRORES DE VALIDACIÓN DETECTADOS =====");
            System.out.println("Total de errores: " + result.getErrorCount());

            // Loggear todos los errores de campos
            result.getFieldErrors().forEach(error -> {
                System.out.println(String.format(
                        "  ❌ Campo: '%s' | Valor rechazado: '%s' | Error: %s",
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ));
            });

            // Loggear errores globales si existen
            if (result.hasGlobalErrors()) {
                System.out.println("\n🔴 Errores globales:");
                result.getGlobalErrors().forEach(error -> {
                    System.out.println("  ❌ " + error.getDefaultMessage());
                });
            }

            // Información del DTO recibido
            System.out.println("\n📋 Datos recibidos:");
            System.out.println("  Título: " + dto.getTitulo());
            System.out.println("  Descripción: " + dto.getDescripcion());
            System.out.println("  Categoría: " + dto.getCategoria());
            System.out.println("  Nivel: " + dto.getNivel());
            System.out.println("  Duración: " + dto.getDuracion());
            System.out.println("  Precio: " + dto.getPrecio());
            System.out.println("  Docente ID: " + dto.getDocenteId());
            System.out.println("  Cantidad de módulos: "
                    + (dto.getModulos() != null ? dto.getModulos().size() : 0));

            // Detalles de módulos y lecciones
            if (dto.getModulos() != null && !dto.getModulos().isEmpty()) {
                System.out.println("\n📚 Detalles de módulos:");
                for (int i = 0; i < dto.getModulos().size(); i++) {
                    var modulo = dto.getModulos().get(i);
                    System.out.println(String.format(
                            "  Módulo [%d]: nombre='%s', orden=%d, lecciones=%d",
                            i,
                            modulo.getNombre(),
                            modulo.getOrden(),
                            modulo.getLecciones() != null ? modulo.getLecciones().size() : 0
                    ));

                    if (modulo.getLecciones() != null) {
                        for (int j = 0; j < modulo.getLecciones().size(); j++) {
                            var leccion = modulo.getLecciones().get(j);
                            System.out.println(String.format(
                                    "    Lección [%d]: título='%s', tipo='%s', orden=%d",
                                    j,
                                    leccion.getTitulo(),
                                    leccion.getTipoContenido(),
                                    leccion.getOrden()
                            ));
                        }
                    }
                }
            }

            System.out.println("🔴 ============================================\n");

            // Crear mensaje de error detallado para el usuario
            String errorDetallado = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Por favor, corrige los errores: " + errorDetallado);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.crearCursoDTO", result);
            redirectAttributes.addFlashAttribute("crearCursoDTO", dto);

            return "redirect:/cursos/crear";
        }

        try {
            // Obtener ID del docente autenticado
            String email = authentication.getName();
            Docente docente = docenteRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));

            dto.setDocenteId(docente.getId());

            // Logging antes de crear
            System.out.println("\n✅ Validación exitosa. Creando curso...");
            System.out.println("  Docente: " + docente.getNombreCompleto());
            System.out.println("  Curso: " + dto.getTitulo());

            // Crear curso
            var cursoCreado = cursoService.crearCurso(dto);

            System.out.println("✅ Curso creado exitosamente con ID: " + cursoCreado.getId() + "\n");

            redirectAttributes.addFlashAttribute("successMessage",
                    "¡Curso creado exitosamente!");

            return "redirect:/cursos/" + cursoCreado.getId();

        } catch (IllegalArgumentException e) {
            System.err.println("🔴 Error al crear curso: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("crearCursoDTO", dto);
            return "redirect:/cursos/crear";
        } catch (Exception e) {
            System.err.println("🔴 Error inesperado al crear curso: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error inesperado: " + e.getMessage());
            redirectAttributes.addFlashAttribute("crearCursoDTO", dto);
            return "redirect:/cursos/crear";
        }
    }

    /**
     * Muestra el formulario para editar un curso existente.
     *
     * @param id ID del curso a editar
     * @param model Modelo para la vista
     * @param authentication Información del usuario autenticado
     * @return Vista del formulario de edición
     */
    @GetMapping("/cursos/{id}/editar")
    public String editarCursoForm(@PathVariable Long id,
            Model model,
            Authentication authentication) {
        // 1. Obtener curso
        CursoDTO curso = cursoService.obtenerCursoPorId(id);

        // 2. Verificar que el docente es el dueño
        String email = authentication.getName();
        Docente docente = docenteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));

        if (!curso.getDocenteId().equals(docente.getId())) {
            throw new AccessDeniedException("No tienes permiso para editar este curso");
        }

        // 3. Convertir a DTO de formulario
        CrearCursoDTO dto = new CrearCursoDTO();
        dto.setTitulo(curso.getTitulo());
        dto.setDescripcion(curso.getDescripcion());
        dto.setCategoria(curso.getCategoria());
        dto.setNivel(curso.getNivel());
        dto.setDuracion(curso.getDuracion());
        dto.setPrecio(curso.getPrecio());
        dto.setDocenteId(curso.getDocenteId());

        model.addAttribute("crearCursoDTO", dto);
        model.addAttribute("curso", curso);

        return "editar-curso";
    }

    /**
     * Procesa la edición de un curso existente.
     *
     * @param id ID del curso a actualizar
     * @param dto Datos actualizados del curso
     * @param result Resultado de la validación
     * @param authentication Información del usuario autenticado
     * @param redirectAttributes Atributos para el redirect
     * @return Redirect a la vista correspondiente
     */
    @PostMapping("/cursos/{id}/editar")
    public String actualizarCurso(@PathVariable Long id,
            @Valid @ModelAttribute CrearCursoDTO dto,
            BindingResult result,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            System.err.println("🔴 Errores de validación al editar curso:");
            result.getAllErrors().forEach(error
                    -> System.err.println("  - " + error.getDefaultMessage())
            );

            redirectAttributes.addFlashAttribute("errorMessage",
                    "Por favor, corrige los errores");
            return "redirect:/cursos/" + id + "/editar";
        }

        try {
            // Verificar permisos (el docente es el dueño)
            String email = authentication.getName();
            Docente docente = docenteRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));

            CursoDTO cursoExistente = cursoService.obtenerCursoPorId(id);
            if (!cursoExistente.getDocenteId().equals(docente.getId())) {
                throw new AccessDeniedException("No tienes permiso");
            }

            // Actualizar
            cursoService.actualizarCurso(id, dto);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Curso actualizado exitosamente");

            return "redirect:/cursos/" + id;

        } catch (Exception e) {
            System.err.println("🔴 Error al actualizar curso: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cursos/" + id + "/editar";
        }
    }
}
