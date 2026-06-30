package com.utp.yachaytinkiy.application.dto;

import java.time.LocalDateTime;
/**
 * DTO para certificados.
 */
public class CertificadoDTO {

    private Long id;
    private String nombreAlumno;
    private String emailAlumno;
    private String tituloCurso;
    private String nombreDocente;
    private String codigoVerificacion;
    private LocalDateTime fechaEmision;
    private String urlPdf;

    // Constructor vacío
    public CertificadoDTO() {
    }

    // Constructor completo
    public CertificadoDTO(Long id, String nombreAlumno, String emailAlumno,
            String tituloCurso, String nombreDocente,
            String codigoVerificacion, LocalDateTime fechaEmision,
            String urlPdf) {
        this.id = id;
        this.nombreAlumno = nombreAlumno;
        this.emailAlumno = emailAlumno;
        this.tituloCurso = tituloCurso;
        this.nombreDocente = nombreDocente;
        this.codigoVerificacion = codigoVerificacion;
        this.fechaEmision = fechaEmision;
        this.urlPdf = urlPdf;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getEmailAlumno() {
        return emailAlumno;
    }

    public void setEmailAlumno(String emailAlumno) {
        this.emailAlumno = emailAlumno;
    }

    public String getTituloCurso() {
        return tituloCurso;
    }

    public void setTituloCurso(String tituloCurso) {
        this.tituloCurso = tituloCurso;
    }

    public String getNombreDocente() {
        return nombreDocente;
    }

    public void setNombreDocente(String nombreDocente) {
        this.nombreDocente = nombreDocente;
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

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }
}
