package com.utp.yachaytinkiy.presentation.view;

import com.utp.yachaytinkiy.application.dto.CertificadoDTO;
import com.utp.yachaytinkiy.application.dto.InscripcionDTO;
import com.utp.yachaytinkiy.application.dto.CursoDTO;
import com.utp.yachaytinkiy.application.service.CertificadoService;
import com.utp.yachaytinkiy.application.service.InscripcionService;
import com.utp.yachaytinkiy.application.service.CursoService;
import com.utp.yachaytinkiy.domain.repository.AlumnoRepository;
import com.utp.yachaytinkiy.domain.repository.DocenteRepository;
import com.utp.yachaytinkiy.domain.model.Alumno;
import com.utp.yachaytinkiy.domain.model.Docente;
import com.utp.yachaytinkiy.application.service.RegistroService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de perfiles de usuario.
 */
@Controller
public class PerfilViewController {

    private final InscripcionService inscripcionService;
    private final CursoService cursoService;
    private final RegistroService registroService;
    private final CertificadoService certificadoService;
    private final AlumnoRepository alumnoRepository;
    private final DocenteRepository docenteRepository;

    public PerfilViewController(InscripcionService inscripcionService,
            CursoService cursoService,
            RegistroService registroService,
            AlumnoRepository alumnoRepository,
            DocenteRepository docenteRepository,
            CertificadoService certificadoService) {
        this.inscripcionService = inscripcionService;
        this.cursoService = cursoService;
        this.registroService = registroService;        
        this.alumnoRepository = alumnoRepository;
        this.docenteRepository = docenteRepository;
        this.certificadoService = certificadoService;
    }

    /**
     * Redirige al perfil según el rol.
     * @param authentication
     * @return 
     */
    @GetMapping("/perfil")
    public String perfil(Authentication authentication) {
        String rol = authentication.getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("ALUMNO");

        if ("DOCENTE".equals(rol)) {
            return "redirect:/perfil/docente";
        } else {
            return "redirect:/perfil/alumno";
        }
    }

    /**
     * Muestra perfil de alumno.
     * @param model
     * @param authentication
     * @return perfil-alumno.html
     */
    @GetMapping("/perfil/alumno")
    public String perfilAlumno(Model model, Authentication authentication) {
        String email = authentication.getName();

        Alumno alumno = alumnoRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));

        List<InscripcionDTO> inscripciones
                = inscripcionService.listarInscripcionesPorAlumno(alumno.getId());

        long cursosEnProgreso = inscripciones.stream()
                .filter(i -> !i.isCompletado())
                .count();

        long cursosCompletados = inscripciones.stream()
                .filter(InscripcionDTO::isCompletado)
                .count();
        // Obtener certificados
        List<CertificadoDTO> certificados
                = certificadoService.listarCertificadosPorAlumno(alumno.getId());

        model.addAttribute("alumno", alumno);
        model.addAttribute("inscripciones", inscripciones);
        model.addAttribute("cursosEnProgreso", cursosEnProgreso);
        model.addAttribute("cursosCompletados", cursosCompletados);
        model.addAttribute("totalHorasEstudiadas", 14); // TODO: Calcular real
        model.addAttribute("certificados", certificados);  

        return "perfil-alumno";
    }

    /**
     * Muestra perfil de docente.
     * @param model
     * @param authentication
     * @return perfil-docente.html
     */
    @GetMapping("/perfil/docente")
    public String perfilDocente(Model model, Authentication authentication) {
        String email = authentication.getName();

        Docente docente = docenteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));

        List<CursoDTO> cursosCreados = cursoService.listarCursosPorDocente(docente.getId());

        model.addAttribute("docente", docente);
        model.addAttribute("cursosCreados", cursosCreados);
        model.addAttribute("totalAlumnos", 0); // TODO: Calcular real

        return "perfil-docente";
    }
    /**
     * Actualiza el perfil del alumno.
     * @param nombreCompleto
     * @param authentication
     * @param redirectAttributes
     * @return redirecciona a /perfil/alumno
     */
    @PostMapping("/perfil/alumno/actualizar")
    public String actualizarPerfilAlumno(
            @RequestParam String nombreCompleto,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        System.out.println("\n📝 ===== ACTUALIZANDO PERFIL ALUMNO =====");

        try {
            // Obtener alumno autenticado
            String email = authentication.getName();
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Alumno no encontrado"
            ));

            System.out.println("  Alumno: " + alumno.getNombreCompleto() + " (ID: " + alumno.getId() + ")");
            System.out.println("  Nuevo nombre: " + nombreCompleto);

            // Actualizar
            registroService.actualizarPerfilAlumno(alumno.getId(), nombreCompleto);

            System.out.println("✅ Perfil actualizado exitosamente");
            System.out.println("📝 ====================================\n");

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("successMessage",
                    "Perfil actualizado exitosamente");

            return "redirect:/perfil/alumno";

        } catch (IllegalArgumentException e) {
            System.err.println("🔴 Error de validación: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/perfil/alumno";

        } catch (Exception e) {
            System.err.println("🔴 Error inesperado: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al actualizar perfil. Intenta nuevamente.");
            return "redirect:/perfil/alumno";
        }
    }

    /**
     * Actualiza el perfil del docente.
     * @param nombreCompleto
     * @param especialidad
     * @param biografia
     * @param authentication
     * @param redirectAttributes
     * @return 
     */
    @PostMapping("/perfil/docente/actualizar")
    public String actualizarPerfilDocente(
            @RequestParam String nombreCompleto,
            @RequestParam String especialidad,
            @RequestParam(required = false) String biografia,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        System.out.println("\n📝 ===== ACTUALIZANDO PERFIL DOCENTE =====");

        try {
            // Obtener docente autenticado
            String email = authentication.getName();
            Docente docente = docenteRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Docente no encontrado"
            ));

            System.out.println("  Docente: " + docente.getNombreCompleto() + " (ID: " + docente.getId() + ")");
            System.out.println("  Nuevo nombre: " + nombreCompleto);
            System.out.println("  Nueva especialidad: " + especialidad);
            System.out.println("  Nueva biografía: " + (biografia != null ? biografia : "Sin cambios"));

            // Actualizar
            registroService.actualizarPerfilDocente(
                    docente.getId(),
                    nombreCompleto,
                    especialidad,
                    biografia
            );

            System.out.println("✅ Perfil actualizado exitosamente");
            System.out.println("📝 ====================================\n");

            // Mensaje de éxito
            redirectAttributes.addFlashAttribute("successMessage",
                    "Perfil actualizado exitosamente");

            return "redirect:/perfil/docente";

        } catch (IllegalArgumentException e) {
            System.err.println("🔴 Error de validación: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/perfil/docente";

        } catch (Exception e) {
            System.err.println("🔴 Error inesperado: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al actualizar perfil. Intenta nuevamente.");
            return "redirect:/perfil/docente";
        }
    }
}
