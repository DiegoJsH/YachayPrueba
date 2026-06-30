package com.utp.yachaytinkiy.presentation.view;

import com.utp.yachaytinkiy.application.dto.ProgresoDTO;
import com.utp.yachaytinkiy.application.service.InscripcionService;
import com.utp.yachaytinkiy.application.service.CursoService;
import com.utp.yachaytinkiy.domain.repository.AlumnoRepository;
import com.utp.yachaytinkiy.domain.model.Alumno;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para el visor de lecciones y aprendizaje del alumno.
 *
 * Responsabilidades: - Mostrar el player de curso con sus lecciones - Controlar
 * la navegación entre lecciones - Validar que el alumno está inscrito antes de
 * acceder
 *
 * @author YachayTinkiy Team
 */
@Controller
@RequestMapping("/cursos")
public class AprendizajeViewController {

    private final InscripcionService inscripcionService;
    private final CursoService cursoService;
    private final AlumnoRepository alumnoRepository;

    public AprendizajeViewController(
            InscripcionService inscripcionService,
            CursoService cursoService,
            AlumnoRepository alumnoRepository) {
        this.inscripcionService = inscripcionService;
        this.cursoService = cursoService;
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * Muestra el visor de lecciones del curso (player).
     *
     * Validaciones: 1. Verifica que el alumno está autenticado 2. Verifica que
     * el alumno está inscrito en el curso 3. Carga el progreso completo del
     * curso
     *
     * Si no hay lección especificada, muestra la primera lección no completada
     * o la primera lección del curso.
     *
     * @param cursoId ID del curso
     * @param leccionId ID de la lección a mostrar (opcional)
     * @param authentication Usuario autenticado
     * @param model Modelo para la vista
     * @param redirectAttributes Atributos para mensajes flash
     * @return Vista del player o redirect si hay error
     */
    @GetMapping("/{cursoId}/aprender")
    public String aprenderCurso(
            @PathVariable Long cursoId,
            @RequestParam(required = false) Long leccionId,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        System.out.println("\n📚 ===== ACCEDIENDO A VISOR DE LECCIONES =====");
        System.out.println("  Curso ID: " + cursoId);
        System.out.println("  Lección ID: " + (leccionId != null ? leccionId : "auto"));

        try {
            // 1. Obtener alumno autenticado
            String email = authentication.getName();
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Alumno no encontrado"
            ));

            System.out.println("  Alumno: " + alumno.getNombreCompleto() + " (ID: " + alumno.getId() + ")");

            // 2. Verificar que está inscrito y obtener progreso
            ProgresoDTO progreso;
            try {
                var inscripcion = inscripcionService.obtenerInscripcion(
                        alumno.getId(),
                        cursoId
                );

                progreso = inscripcionService.obtenerProgreso(inscripcion.getId());

                System.out.println("  ✅ Inscripción válida (ID: " + inscripcion.getId() + ")");
                System.out.println("  Progreso: " + progreso.getPorcentajeProgreso() + "%");

            } catch (Exception e) {
                System.err.println("  ❌ No inscrito en el curso");
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Debes inscribirte al curso para acceder a las lecciones");
                return "redirect:/cursos/" + cursoId;
            }

            // 3. Si no se especificó lección, buscar la primera no completada
            Long leccionActualId = leccionId;
            if (leccionActualId == null) {
                leccionActualId = encontrarPrimeraLeccionNoCompletada(progreso);
                System.out.println("  Lección automática: " + leccionActualId);
            }

            // 4. Agregar datos al modelo
            model.addAttribute("progreso", progreso);
            model.addAttribute("leccionActualId", leccionActualId);
            model.addAttribute("cursoId", cursoId);
            model.addAttribute("inscripcionId", progreso.getInscripcionId());

            System.out.println("📚 ==========================================\n");

            return "curso-player";

        } catch (Exception e) {
            System.err.println("🔴 Error al cargar visor: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al cargar el curso. Por favor, intenta nuevamente.");
            return "redirect:/mis-cursos";
        }
    }

    /**
     * Encuentra la primera lección no completada del curso. Si todas están
     * completadas, retorna la primera lección.
     *
     * @param progreso DTO con el progreso completo del curso
     * @return ID de la lección a mostrar
     */
    private Long encontrarPrimeraLeccionNoCompletada(ProgresoDTO progreso) {
        // Buscar primera lección no completada
        for (var modulo : progreso.getModulos()) {
            for (var leccion : modulo.getLecciones()) {
                if (!leccion.isCompletada()) {
                    return leccion.getLeccionId();
                }
            }
        }

        // Si todas completadas, retornar la primera
        if (!progreso.getModulos().isEmpty()
                && !progreso.getModulos().get(0).getLecciones().isEmpty()) {
            return progreso.getModulos().get(0).getLecciones().get(0).getLeccionId();
        }

        return null;
    }
    /**
     * Marca una lección como completada y actualiza el progreso.
     *
     * Este endpoint procesa la solicitud POST cuando el alumno marca
     * manualmente una lección como completada.
     *
     * Retorna JSON con el progreso actualizado para actualizar la UI sin
     * recargar la página.
     *
     * @param inscripcionId ID de la inscripción
     * @param leccionId ID de la lección a marcar como completada
     * @param authentication Usuario autenticado
     * @return JSON con progreso actualizado
     */
    @PostMapping("/inscripciones/{inscripcionId}/lecciones/{leccionId}/completar")
    @ResponseBody
    public ProgresoDTO marcarLeccionCompletada(
            @PathVariable Long inscripcionId,
            @PathVariable Long leccionId,
            Authentication authentication) {

        System.out.println("\n✅ ===== MARCANDO LECCIÓN COMPLETADA =====");
        System.out.println("  Inscripción ID: " + inscripcionId);
        System.out.println("  Lección ID: " + leccionId);

        try {
            // 1. Obtener alumno autenticado
            String email = authentication.getName();
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

            System.out.println("  Alumno: " + alumno.getNombreCompleto());

            // 2. Marcar lección como completada
            inscripcionService.marcarLeccionCompletada(inscripcionId, leccionId);

            System.out.println("  ✅ Lección marcada exitosamente");

            // 3. Obtener progreso actualizado
            ProgresoDTO progresoActualizado = inscripcionService.obtenerProgreso(inscripcionId);

            System.out.println("  Progreso actualizado: " + progresoActualizado.getPorcentajeProgreso() + "%");
            System.out.println("  Lecciones completadas: " + progresoActualizado.getLeccionesCompletadas()
                    + "/" + progresoActualizado.getTotalLecciones());
            System.out.println("✅ ==========================================\n");

            // 4. Retornar progreso en JSON
            return progresoActualizado;

        } catch (IllegalStateException e) {
            // Lección ya completada
            System.err.println("⚠️ Lección ya estaba completada");
            throw e;
        } catch (Exception e) {
            System.err.println("🔴 Error al marcar lección: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al marcar lección como completada", e);
        }
    }
    /**
     * Endpoint para obtener el progreso actualizado del curso.
     *
     * Usado por AJAX para actualizar la UI después de marcar lecciones 
     *
     * @param cursoId ID del curso
     * @param authentication Usuario autenticado
     * @param model Modelo para la vista
     * @return Vista parcial con el progreso actualizado
     */
    @GetMapping("/{cursoId}/progreso")
    public String obtenerProgreso(
            @PathVariable Long cursoId,
            Authentication authentication,
            Model model) {

        try {
            String email = authentication.getName();
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

            var inscripcion = inscripcionService.obtenerInscripcion(
                    alumno.getId(),
                    cursoId
            );

            ProgresoDTO progreso = inscripcionService.obtenerProgreso(inscripcion.getId());

            model.addAttribute("progreso", progreso);

            return "fragments/progreso :: progreso-sidebar";

        } catch (Exception e) {
            System.err.println("🔴 Error al obtener progreso: " + e.getMessage());
            return "error";
        }
    }
}
