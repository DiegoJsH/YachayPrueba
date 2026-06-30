package com.utp.yachaytinkiy.application.dto;

public class AlumnoBasicoDTO {

    private Long id;
    private String nombreCompleto;
    private String email;

    // Constructor vacío
    public AlumnoBasicoDTO() {
    }

    // Constructor completo
    public AlumnoBasicoDTO(Long id, String nombreCompleto, String email) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
