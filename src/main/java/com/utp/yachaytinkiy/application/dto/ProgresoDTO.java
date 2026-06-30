package com.utp.yachaytinkiy.application.dto;

import java.util.List;

public class ProgresoDTO {

    private Long inscripcionId;
    private String tituloCurso;
    private float porcentajeProgreso;
    private int leccionesCompletadas;
    private int totalLecciones;
    private boolean completado;
    private List<ModuloProgresoDTO> modulos;

    // Constructor vacío
    public ProgresoDTO() {
    }

    // Getters y Setters
    public Long getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Long inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public float getPorcentajeProgreso() {
        return porcentajeProgreso;
    }

    public void setPorcentajeProgreso(float porcentajeProgreso) {
        this.porcentajeProgreso = porcentajeProgreso;
    }

    public int getLeccionesCompletadas() {
        return leccionesCompletadas;
    }

    public void setLeccionesCompletadas(int leccionesCompletadas) {
        this.leccionesCompletadas = leccionesCompletadas;
    }

    public int getTotalLecciones() {
        return totalLecciones;
    }

    public void setTotalLecciones(int totalLecciones) {
        this.totalLecciones = totalLecciones;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }

    public List<ModuloProgresoDTO> getModulos() {
        return modulos;
    }

    public void setModulos(List<ModuloProgresoDTO> modulos) {
        this.modulos = modulos;
    }
}
