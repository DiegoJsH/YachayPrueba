package com.utp.yachaytinkiy.application.dto;

import java.time.LocalDateTime;

public class InscripcionResponseDTO {

    private Long id;
    private AlumnoBasicoDTO alumno;
    private CursoBasicoDTO curso;
    private LocalDateTime fechaInscripcion;
    private float progreso;

    // Constructor vacío
    public InscripcionResponseDTO() {
    }

    // Constructor completo
    public InscripcionResponseDTO(Long id, AlumnoBasicoDTO alumno,
            CursoBasicoDTO curso, LocalDateTime fechaInscripcion,
            float progreso) {
        this.id = id;
        this.alumno = alumno;
        this.curso = curso;
        this.fechaInscripcion = fechaInscripcion;
        this.progreso = progreso;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AlumnoBasicoDTO getAlumno() {
        return alumno;
    }

    public void setAlumno(AlumnoBasicoDTO alumno) {
        this.alumno = alumno;
    }

    public CursoBasicoDTO getCurso() {
        return curso;
    }

    public void setCurso(CursoBasicoDTO curso) {
        this.curso = curso;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDateTime fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public float getProgreso() {
        return progreso;
    }

    public void setProgreso(float progreso) {
        this.progreso = progreso;
    }
}
