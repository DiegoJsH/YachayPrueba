package com.utp.yachaytinkiy.application.dto;

import jakarta.validation.constraints.*;

public class RegistroDocenteDTO {

    @Email(message = "Email debe ser válido")
    @NotBlank(message = "Email es obligatorio")
    private String email;

    @NotBlank(message = "Password es obligatorio")
    @Size(min = 8)
    private String password;

    @NotBlank(message = "Nombre completo es obligatorio")
    private String nombreCompleto;

    @NotBlank(message = "Especialidad es obligatoria")
    private String especialidad;

    @NotBlank(message = "Grado académico es obligatorio")
    private String gradoAcademico;

    @Size(max = 300, message = "Biografía no debe exceder 300 caracteres")
    private String biografia;

    // Constructor vacío
    public RegistroDocenteDTO() {
    }

    // Constructor completo
    public RegistroDocenteDTO(String email, String password, String nombreCompleto,String especialidad, String gradoAcademico, String biografia) {
        this.email = email;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
        this.gradoAcademico = gradoAcademico;
        this.biografia = biografia;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getGradoAcademico() {
        return gradoAcademico;
    }

    public void setGradoAcademico(String gradoAcademico) {
        this.gradoAcademico = gradoAcademico;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
}
