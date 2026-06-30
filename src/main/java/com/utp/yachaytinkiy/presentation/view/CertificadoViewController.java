package com.utp.yachaytinkiy.presentation.view;

import com.utp.yachaytinkiy.application.dto.CertificadoDTO;
import com.utp.yachaytinkiy.application.service.CertificadoService;
import com.utp.yachaytinkiy.domain.repository.AlumnoRepository;
import com.utp.yachaytinkiy.domain.model.Alumno;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para gestión de certificados.
 */
@Controller
@RequestMapping("/certificados")
public class CertificadoViewController {

    private final CertificadoService certificadoService;
    private final AlumnoRepository alumnoRepository;

    public CertificadoViewController(CertificadoService certificadoService,
            AlumnoRepository alumnoRepository) {
        this.certificadoService = certificadoService;
        this.alumnoRepository = alumnoRepository;
    }

    /**
     * Lista los certificados del alumno autenticado.
     */
    @GetMapping
    public String listarCertificados(Model model, Authentication authentication) {

        System.out.println("\n🎓 ===== LISTANDO CERTIFICADOS =====");

        try {
            // Obtener alumno autenticado
            String email = authentication.getName();
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Alumno no encontrado"
            ));

            System.out.println("  Alumno: " + alumno.getNombreCompleto());

            // Obtener certificados
            List<CertificadoDTO> certificados
                    = certificadoService.listarCertificadosPorAlumno(alumno.getId());

            System.out.println("  Total certificados: " + certificados.size());
            System.out.println("🎓 ===================================\n");

            model.addAttribute("certificados", certificados);
            model.addAttribute("alumno", alumno);

            return "certificados";

        } catch (Exception e) {
            System.err.println("🔴 Error al listar certificados: " + e.getMessage());
            model.addAttribute("errorMessage",
                    "Error al cargar certificados. Intenta nuevamente.");
            return "error";
        }
    }

    /**
     * Genera certificado manualmente (si no se generó automáticamente).
     */
    @PostMapping("/generar")
    public String generarCertificado(
            @RequestParam Long cursoId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        System.out.println("\n🎓 ===== GENERANDO CERTIFICADO MANUAL =====");

        try {
            // Obtener alumno autenticado
            String email = authentication.getName();
            Alumno alumno = alumnoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(
                    "Alumno no encontrado"
            ));

            // Generar certificado
            CertificadoDTO certificado = certificadoService.generarCertificado(
                    alumno.getId(),
                    cursoId
            );

            redirectAttributes.addFlashAttribute("successMessage",
                    "¡Certificado generado exitosamente!");

            return "redirect:/certificados";

        } catch (IllegalArgumentException e) {
            System.err.println("Error de validación: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/perfil/alumno";

        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Error al generar certificado. Intenta nuevamente.");
            return "redirect:/perfil/alumno";
        }
    }
}
