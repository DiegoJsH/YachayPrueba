package com.utp.yachaytinkiy.presentation.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para la página principal.
 */
@Controller
public class HomeViewController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
}
