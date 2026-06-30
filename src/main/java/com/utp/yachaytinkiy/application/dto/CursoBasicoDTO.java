package com.utp.yachaytinkiy.application.dto;

public class CursoBasicoDTO {

    private Long id;
    private String titulo;
    private String categoria;
    private String nivel;

    // Constructor vacío
    public CursoBasicoDTO() {
    }

    // Constructor completo
    public CursoBasicoDTO(Long id, String titulo, String categoria, String nivel) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.nivel = nivel;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }
}
