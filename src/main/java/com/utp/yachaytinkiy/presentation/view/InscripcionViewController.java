package com.utp.yachaytinkiy.presentation.view;

import com.utp.yachaytinkiy.application.dto.InscripcionDTO;
import com.utp.yachaytinkiy.application.dto.InscripcionResponseDTO;
import com.utp.yachaytinkiy.application.service.InscripcionService;
import com.utp.yachaytinkiy.application.service.CursoService;
import com.utp.yachaytinkiy.domain.repository.AlumnoRepository;
import com.utp.yachaytinkiy.domain.model.Alumno;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para gestionar las inscripciones de alumnos a cursos.
 *
 * Responsabilidades: - Procesar inscripciones de alumnos a cursos - Listar
 * cursos inscritos del alumno - Validar permisos y reglas de negocio de
 * inscripción
 *
 * @author YachayTinkiy Team
 */
@Controller
@RequestMapping
public class InscripcionViewController {

    private final InscripcionService inscripcionService;
    private final CursoService cursoService;
    private final AlumnoRepository alumnoRepository;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param inscripcionService Servicio de inscripciones
     * @param cursoService Servicio de cursos
     * @param alumnoRepository Repositorio de alumnos
     */
    public InscripcionViewController(
            InscripcionService inscripcionService,
            CursoService cursoService,
            AlumnoRepository alumnoRepository) {
        this.inscripcionService = inscripcionService;
        this.cursoService = cursoService;
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * Procesa la inscripción de un alumno a un curso.
     *
     * Validaciones implementadas: 1. Verifica que el alumno existe 2. Verifica
     * que el curso existe 3. Verifica que el alumno no está ya inscrito
     * (manejado en el servicio)
     *
     * @param cursoId ID del curso al que se inscribe
     * @param authentication Información del usuario autenticado
     * @param redirectAttributes Atributos para mensajes flash
     * @return Redirect a la página correspondiente
     */
    @PostMapping("/cursos/{id}/inscribirse")
    public String inscribirseACurso(
            @PathVariable("id") Long cursoId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        System.out.println("\n🎓 ===== PROCESANDO INSCRIPCIÓN =====");
        System.out.println("  Curso ID: " + cursoId);

        try {
            // 1. Obtener email del alumno autenticado
            String email = authentication.getName();
            System.out.println("  Email alumno: " + email);

            // 2. Validar que el alumno existe
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Alumno no encontrado con email: " + email
            ));

            System.out.println("  Alumno encontrado: " + alumno.getNombreCompleto() + " (ID: " + alumno.getId() + ")");

            // 3. Validar que el curso existe (se hace en el servicio)
            // 4. Validar que NO está ya inscrito (se hace en el servicio)
            // 5. Procesar inscripción
            InscripcionResponseDTO inscripcion = inscripcionService.inscribirAlumno(
                    alumno.getId(),
                    cursoId
            );

            System.out.println("  ✅ Inscripción exitosa con ID: " + inscripcion.getId());
            System.out.println("  Curso: " + inscripcion.getCurso().getTitulo());
            System.out.println("  Fecha: " + inscripcion.getFechaInscripcion());
            System.out.println("🎓 ====================================\n");

            // 6. Mensaje de éxito
            redirectAttributes.addFlashAttribute("successMessage",
                    "¡Te has inscrito exitosamente al curso: " + inscripcion.getCurso().getTitulo() + "!");

            // 7. Redirect al perfil del alumno (donde verá sus cursos)
            return "redirect:/perfil/alumno";

        } catch (IllegalStateException e) {
            // Error de negocio (ej: ya está inscrito)
            System.err.println("🔴 Error de negocio: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cursos/" + cursoId;

        } catch (IllegalArgumentException e) {
            // Error de validación (curso/alumno no existe)
            System.err.println("🔴 Error de validación: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cursos/" + cursoId;

        } catch (Exception e) {
            // Error inesperado
            System.err.println("🔴 Error inesperado al procesar inscripción: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al procesar la inscripción. Por favor, intenta nuevamente.");
            return "redirect:/cursos/" + cursoId;
        }
    }

    /**
     * Muestra la lista de cursos en los que está inscrito el alumno.
     *
     * Esta vista se renderiza dentro del perfil del alumno (perfil-alumno.html)
     * a través del PerfilViewController.
     *
     * Este endpoint podría usarse para una vista dedicada de "Mis Cursos" en el
     * futuro.
     *
     * @param model Modelo para la vista
     * @param authentication Información del usuario autenticado
     * @return Vista de cursos inscritos
     */
    @GetMapping("/mis-cursos")
    public String misCursos(Model model, Authentication authentication) {

        System.out.println("\n📚 ===== LISTANDO MIS CURSOS =====");

        try {
            // 1. Obtener email del alumno autenticado
            String email = authentication.getName();
            System.out.println("  Email: " + email);

            // 2. Buscar alumno
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Alumno no encontrado con email: " + email
            ));

            // 3. Obtener inscripciones del alumno
            List<InscripcionDTO> inscripciones
                    = inscripcionService.listarInscripcionesPorAlumno(alumno.getId());

            System.out.println("  Total de cursos inscritos: " + inscripciones.size());

            // 4. Calcular estadísticas
            long cursosEnProgreso = inscripciones.stream()
                    .filter(i -> !i.isCompletado())
                    .count();

            long cursosCompletados = inscripciones.stream()
                    .filter(InscripcionDTO::isCompletado)
                    .count();

            System.out.println("  En progreso: " + cursosEnProgreso);
            System.out.println("  Completados: " + cursosCompletados);
            System.out.println("📚 =================================\n");

            // 5. Agregar datos al modelo
            model.addAttribute("alumno", alumno);
            model.addAttribute("inscripciones", inscripciones);
            model.addAttribute("cursosEnProgreso", cursosEnProgreso);
            model.addAttribute("cursosCompletados", cursosCompletados);
            model.addAttribute("totalHorasEstudiadas", 0); // TODO: Calcular real

            return "mis-cursos";

        } catch (Exception e) {
            System.err.println("🔴 Error al listar cursos: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage",
                    "Error al cargar tus cursos. Por favor, intenta nuevamente.");
            return "error";
        }
    }
}
