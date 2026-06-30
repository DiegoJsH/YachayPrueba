package com.utp.yachaytinkiy.application.dto;

import java.time.LocalDateTime;

public class LeccionProgresoDTO {

    private Long leccionId;
    private String titulo;
    private String tipoContenido;
    private int orden;
    private boolean completada;
    private LocalDateTime fechaCompletado;
    private String urlContenido;
    private String descripcion;

    // Constructor vacío
    public LeccionProgresoDTO() {
    }

    //  constructor completo:
    public LeccionProgresoDTO(Long leccionId, String titulo, String tipoContenido,
            int orden, boolean completada, LocalDateTime fechaCompletado,
            String urlContenido, String descripcion) {
        this.leccionId = leccionId;
        this.titulo = titulo;
        this.tipoContenido = tipoContenido;
        this.orden = orden;
        this.completada = completada;
        this.fechaCompletado = fechaCompletado;
        this.urlContenido = urlContenido;
        this.descripcion = descripcion;
    }

    // getters y setters:
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
    
    public Long getLeccionId() {
        return leccionId;
    }

    public void setLeccionId(Long leccionId) {
        this.leccionId = leccionId;
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

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }
}
