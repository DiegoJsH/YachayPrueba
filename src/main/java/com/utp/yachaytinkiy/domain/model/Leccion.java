package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "lecciones")
public class Leccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título de la lección es obligatorio")
    @Size(max = 150, message = "Título no debe exceder 150 caracteres")
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoContenido tipoContenido;

    @Min(value = 1, message = "Orden debe ser mayor a 0")
    private int orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;

    @Column(length = 500)
    private String urlContenido;

    @Column(length = 1000)
    private String descripcion;

    // Constructor para JPA
    protected Leccion() {
    }

    // Constructor principal
    public Leccion(String titulo, TipoContenido tipoContenido, int orden, Modulo modulo) {
        this.setTitulo(titulo);
        this.tipoContenido = tipoContenido;
        this.setOrden(orden);
        this.modulo = modulo;
    }

    // Métodos de validación
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título de la lección no puede estar vacío");
        }
        this.titulo = titulo;
    }

    public void setOrden(int orden) {
        if (orden < 1) {
            throw new IllegalArgumentException("Orden debe ser mayor a 0");
        }
        this.orden = orden;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public TipoContenido getTipoContenido() {
        return tipoContenido;
    }

    public void setTipoContenido(TipoContenido tipoContenido) {
        this.tipoContenido = tipoContenido;
    }

    public int getOrden() {
        return orden;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

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
}
