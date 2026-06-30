package com.utp.yachaytinkiy.application.dto;

import com.utp.yachaytinkiy.domain.model.RolUsuario;

public class UsuarioAutenticadoDTO {

    private Long id;
    private String email;
    private String nombreCompleto;
    private RolUsuario rol;
    private String token; // Para JWT (opcional por ahora)

    public UsuarioAutenticadoDTO() {
    }

    public UsuarioAutenticadoDTO(Long id, String email, String nombreCompleto,
            RolUsuario rol) {
        this.id = id;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public RolUsuario getRol() {
        return rol;
    }

    public void setRol(RolUsuario rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
