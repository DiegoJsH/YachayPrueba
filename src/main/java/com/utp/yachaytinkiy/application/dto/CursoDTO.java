package com.utp.yachaytinkiy.application.dto;

import java.math.BigDecimal;

import java.time.LocalDateTime;

public class CursoDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String nivel;
    private Integer duracion;
    private BigDecimal precio;
    private String nombreDocente;
    private Long docenteId;
    private int cantidadModulos;
    private int cantidadLecciones;
    private LocalDateTime fechaCreacion;

    // Constructor vacío
    public CursoDTO() {
    }

    // Constructor completo
    public CursoDTO(Long id, String titulo, String descripcion, String categoria,
            String nivel, Integer duracion, BigDecimal precio,
            String nombreDocente, Long docenteId, int cantidadModulos,
            int cantidadLecciones, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nivel = nivel;
        this.duracion = duracion;
        this.precio = precio;
        this.nombreDocente = nombreDocente;
        this.docenteId = docenteId;
        this.cantidadModulos = cantidadModulos;
        this.cantidadLecciones = cantidadLecciones;
        this.fechaCreacion = fechaCreacion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
    }

    public Long getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Long docenteId) {
        this.docenteId = docenteId;
    }

    public int getCantidadModulos() {
        return cantidadModulos;
    }

    public void setCantidadModulos(int cantidadModulos) {
        this.cantidadModulos = cantidadModulos;
    }

    public int getCantidadLecciones() {
        return cantidadLecciones;
    }

    public void setCantidadLecciones(int cantidadLecciones) {
        this.cantidadLecciones = cantidadLecciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
