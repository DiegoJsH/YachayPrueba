package com.utp.yachaytinkiy.application.dto;

import java.time.LocalDateTime;

public class InscripcionDTO {

    private Long id;
    private Long alumnoId;
    private String nombreAlumno;
    private Long cursoId;
    private String tituloCurso;
    private float progreso;
    private LocalDateTime fechaInscripcion;
    private int leccionesCompletadas;
    private int totalLecciones;
    private boolean completado;

    // Constructor vacío
    public InscripcionDTO() {
    }

    // Constructor completo
    public InscripcionDTO(Long id, Long alumnoId, String nombreAlumno,
            Long cursoId, String tituloCurso, float progreso,
            LocalDateTime fechaInscripcion, int leccionesCompletadas,
            int totalLecciones, boolean completado) {
        this.id = id;
        this.alumnoId = alumnoId;
        this.nombreAlumno = nombreAlumno;
        this.cursoId = cursoId;
        this.tituloCurso = tituloCurso;
        this.progreso = progreso;
        this.fechaInscripcion = fechaInscripcion;
        this.leccionesCompletadas = leccionesCompletadas;
        this.totalLecciones = totalLecciones;
        this.completado = completado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public float getProgreso() {
        return progreso;
    }

    public void setProgreso(float progreso) {
        this.progreso = progreso;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
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
}
