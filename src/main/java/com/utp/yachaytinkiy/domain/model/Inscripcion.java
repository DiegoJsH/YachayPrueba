package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaInscripcion;

    @Column(nullable = false)
    private float progreso; // 0-100

    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgresoLeccion> leccionesCompletadas;

    // Constructor para JPA
    protected Inscripcion() {
        this.fechaInscripcion = LocalDateTime.now();
        this.progreso = 0.0f;
        this.leccionesCompletadas = new ArrayList<>();
    }

    // Constructor principal
    public Inscripcion(Alumno alumno, Curso curso) {
        this();
        this.alumno = alumno;
        this.curso = curso;
    }

    // Métodos de negocio
    public void marcarLeccionCompletada(Leccion leccion) {
        // Validar que la lección pertenece al curso
        validarLeccionDelCurso(leccion);

        // Verificar si ya está completada
        if (esLeccionCompletada(leccion)) {
            throw new IllegalStateException(
                    "La leccion ya esta marcada como completada"
            );
        }

        // Crear el progreso de lección
        ProgresoLeccion progresoLeccion = new ProgresoLeccion(this, leccion);
        leccionesCompletadas.add(progresoLeccion);
    }

    private void validarLeccionDelCurso(Leccion leccion) {
        boolean leccionPerteneceCurso = curso.getModulos().stream()
                .flatMap(m -> m.getLecciones().stream())
                .anyMatch(l -> l.equals(leccion));

        if (!leccionPerteneceCurso) {
            throw new IllegalArgumentException(
                    "La leccion no pertenece a este curso"
            );
        }
    }

    public boolean esLeccionCompletada(Leccion leccion) {
        return leccionesCompletadas.stream()
                .anyMatch(pl -> pl.getLeccion().equals(leccion));
    }

    public void actualizarProgreso() {
        int totalLecciones = curso.getCantidadLecciones();

        if (totalLecciones == 0) {
            this.progreso = 0.0f;
            return;
        }

        int leccionesCompletas = leccionesCompletadas.size();
        this.progreso = ((float) leccionesCompletas / totalLecciones) * 100;
    }

    public boolean estaCompleto() {
        return progreso >= 100.0f;
    }

    public int getCantidadLeccionesCompletadas() {
        return leccionesCompletadas.size();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public LocalDateTime getFechaInscripcion() {
        return fechaInscripcion;
    }

    public float getProgreso() {
        return progreso;
    }

    public List<ProgresoLeccion> getLeccionesCompletadas() {
        return new ArrayList<>(leccionesCompletadas);
    }
}
