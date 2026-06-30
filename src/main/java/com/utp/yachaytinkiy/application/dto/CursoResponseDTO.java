package com.utp.yachaytinkiy.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CursoResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String nivel;
    private Integer duracion;
    private BigDecimal precio;
    private DocenteBasicoDTO docente; // ← Solo info básica del docente
    private LocalDateTime fechaCreacion;
    private List<ModuloDTO> modulos;
    private int cantidadModulos;
    private int cantidadLecciones;
    
    // Constructor vacío
    public CursoResponseDTO() {
    }

    // Constructor completo
    public CursoResponseDTO(Long id, String titulo, String descripcion,
            String categoria, String nivel, Integer duracion,
            BigDecimal precio, DocenteBasicoDTO docente,
            LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nivel = nivel;
        this.duracion = duracion;
        this.precio = precio;
        this.docente = docente;
        this.fechaCreacion = fechaCreacion;
    }
    
    // getters y setters
    public List<ModuloDTO> getModulos() {
        return modulos;
    }

    public void setModulos(List<ModuloDTO> modulos) {
        this.modulos = modulos;
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

    public DocenteBasicoDTO getDocente() {
        return docente;
    }

    public void setDocente(DocenteBasicoDTO docente) {
        this.docente = docente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
