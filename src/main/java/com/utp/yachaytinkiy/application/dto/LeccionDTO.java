package com.utp.yachaytinkiy.application.dto;

public class LeccionDTO {

    private Long id;
    private String titulo;
    private String tipoContenido;
    private int orden;
    private String urlContenido;
    private String descripcion;

    public LeccionDTO() {
    }

    public LeccionDTO(Long id, String titulo, String tipoContenido,
            int orden, String urlContenido, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.tipoContenido = tipoContenido;
        this.orden = orden;
        this.urlContenido = urlContenido;
        this.descripcion = descripcion;
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

    public String getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getUrlContenido() {
        return urlContenido;
    }

    public void setUrlContenido(String urlContenido) {
        this.urlContenido = urlContenido;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
