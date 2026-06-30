package com.utp.yachaytinkiy.application.dto;

import jakarta.validation.constraints.*;

public class LoginDTO {

    @Email(message = "Email debe ser válido")
    @NotBlank(message = "Email es obligatorio")
    private String email;

    @NotBlank(message = "Password es obligatorio")
    private String password;

    @NotBlank(message = "Rol es obligatorio")
    private String rol; // "ALUMNO" o "DOCENTE"

    public LoginDTO() {
    }

    public LoginDTO(String email, String password, String rol) {
        this.email = email;
        this.password = password;
        this.rol = rol;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
