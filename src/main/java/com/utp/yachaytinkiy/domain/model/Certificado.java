package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificados")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(unique = true, nullable = false)
    private String codigoVerificacion;

    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @Column(length = 1000)
    private String urlPdf;

    // Constructor para JPA
    protected Certificado() {
        this.fechaEmision = LocalDateTime.now();
    }

    // Constructor principal
    public Certificado(Alumno alumno, Curso curso, String codigoVerificacion) {
        this();
        this.alumno = alumno;
        this.curso = curso;
        this.codigoVerificacion = codigoVerificacion;
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

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }
}
