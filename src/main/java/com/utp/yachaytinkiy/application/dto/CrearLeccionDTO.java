package com.utp.yachaytinkiy.application.dto;

import jakarta.validation.constraints.*;

/**
 * DTO para crear una lección.
 */
public class CrearLeccionDTO {

    @NotBlank(message = "Título de la lección es obligatorio")
    @Size(max = 150, message = "Título no debe exceder 150 caracteres")
    private String titulo;

    @NotBlank(message = "Tipo de contenido es obligatorio")
    private String tipoContenido; // "VIDEO", "TEXTO", "PDF", "QUIZ"

    @Min(value = 1, message = "Orden debe ser mayor a 0")
    private int orden;

    @Size(max = 500, message = "URL no debe exceder 500 caracteres")
    private String urlContenido;

    @Size(max = 1000, message = "Descripción no debe exceder 1000 caracteres")
    private String descripcion;

    // Constructores
    public CrearLeccionDTO() {
    }

    public CrearLeccionDTO(String titulo, String tipoContenido, int orden) {
        this.titulo = titulo;
        this.tipoContenido = tipoContenido;
        this.orden = orden;
    }

    // Getters y Setters
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
