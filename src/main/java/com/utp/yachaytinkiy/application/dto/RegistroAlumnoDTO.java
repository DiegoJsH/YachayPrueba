package com.utp.yachaytinkiy.application.dto;

import jakarta.validation.constraints.*;

public class RegistroAlumnoDTO {

    @Email(message = "Email debe ser válido")
    @NotBlank(message = "Email es obligatorio")
    private String email;

    @NotBlank(message = "Password es obligatorio")
    @Size(min = 8, message = "Password debe tener mínimo 8 caracteres")
    private String password;

    @NotBlank(message = "Nombre completo es obligatorio")
    @Size(min = 3, max = 100)
    private String nombreCompleto;

    // Constructores
    public RegistroAlumnoDTO() {
    }

    public RegistroAlumnoDTO(String email, String password, String nombreCompleto) {
        this.email = email;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
}
