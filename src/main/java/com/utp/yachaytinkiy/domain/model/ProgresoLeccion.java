package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso_lecciones")
public class ProgresoLeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Inscripcion inscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leccion_id", nullable = false)
    private Leccion leccion;

    @Column(nullable = false)
    private boolean completada;

    @Column(nullable = false)
    private LocalDateTime fechaCompletado;

    // Constructor para JPA
    protected ProgresoLeccion() {
        this.completada = true;
        this.fechaCompletado = LocalDateTime.now();
    }

    // Constructor principal
    public ProgresoLeccion(Inscripcion inscripcion, Leccion leccion) {
        this();
        this.inscripcion = inscripcion;
        this.leccion = leccion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public Inscripcion getInscripcion() {
        return inscripcion;
    }

    public void setInscripcion(Inscripcion inscripcion) {
        this.inscripcion = inscripcion;
    }

    public Leccion getLeccion() {
        return leccion;
    }

    public void setLeccion(Leccion leccion) {
        this.leccion = leccion;
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
