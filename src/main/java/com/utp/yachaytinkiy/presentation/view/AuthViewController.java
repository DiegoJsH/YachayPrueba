package com.utp.yachaytinkiy.presentation.view;

import com.utp.yachaytinkiy.application.dto.RegistroAlumnoDTO;
import com.utp.yachaytinkiy.application.dto.RegistroDocenteDTO;
import com.utp.yachaytinkiy.application.service.RegistroService;
import com.utp.yachaytinkiy.domain.model.Alumno;
import com.utp.yachaytinkiy.domain.model.Docente;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador de autenticación y registro.
 */
@Controller
public class AuthViewController {

    private final RegistroService registroService;

    public AuthViewController(RegistroService registroService) {
        this.registroService = registroService;
    }

    /**
     * Muestra la página de login.
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "Email o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Has cerrado sesión exitosamente");
        }

        return "login";
    }

    /**
     * Muestra formulario de registro de alumno.
     */
    @GetMapping("/registro/alumno")
    public String registroAlumnoPage(Model model) {
        model.addAttribute("registroAlumnoDTO", new RegistroAlumnoDTO());
        return "registro-alumno";
    }

    /**
     * Procesa registro de alumno.
     */
    @PostMapping("/registro/alumno")
    public String registrarAlumno(
            @Valid @ModelAttribute RegistroAlumnoDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Por favor, corrige los errores en el formulario");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registroAlumnoDTO", result);
            redirectAttributes.addFlashAttribute("registroAlumnoDTO", dto);
            return "redirect:/registro/alumno";
        }

        try {
            Alumno alumno = registroService.registrarAlumno(dto);
            return "redirect:/login?registered=true";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("registroAlumnoDTO", dto);
            return "redirect:/registro/alumno";
        }
    }

    /**
     * Muestra formulario de registro de docente.
     */
    @GetMapping("/registro/docente")
    public String registroDocentePage(Model model) {
        model.addAttribute("registroDocenteDTO", new RegistroDocenteDTO());
        return "registro-docente";
    }

    /**
     * Procesa registro de docente.
     */
    @PostMapping("/registro/docente")
    public String registrarDocente(
            @Valid @ModelAttribute RegistroDocenteDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Por favor, corrige los errores en el formulario");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registroDocenteDTO", result);
            redirectAttributes.addFlashAttribute("registroDocenteDTO", dto);
            return "redirect:/registro/docente";
        }

        try {
            Docente docente = registroService.registrarDocente(dto);
            return "redirect:/login?registered=true";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("registroDocenteDTO", dto);
            return "redirect:/registro/docente";
        }
    }
}
