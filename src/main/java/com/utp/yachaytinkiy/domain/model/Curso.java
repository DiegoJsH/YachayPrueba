package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Título es obligatorio")
    @Size(max = 80, message = "Título no debe exceder 80 caracteres")
    @Column(nullable = false, length = 80)
    private String titulo;

    @NotBlank(message = "Descripción es obligatoria")
    @Size(max = 500, message = "Descripción no debe exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;

    @NotBlank(message = "Categoría es obligatoria")
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelCurso nivel;

    @Min(value = 1, message = "Duración mínima es 1 hora")
    @Max(value = 200, message = "Duración máxima es 200 horas")
    private Integer duracion; // en horas

    @DecimalMin(value = "0.0", message = "Precio no puede ser negativo")
    private BigDecimal precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Modulo> modulos;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private String urlPortada;

    // Constructor para JPA
    protected Curso() {
        this.modulos = new ArrayList<>();
        this.fechaCreacion = LocalDateTime.now();
        this.precio = BigDecimal.ZERO;
    }

    // Constructor principal
    public Curso(String titulo, String descripcion, String categoria,
            NivelCurso nivel, Docente docente) {
        this();
        this.setTitulo(titulo);
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.nivel = nivel;
        this.docente = docente;
    }

    // Métodos de negocio
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título no puede estar vacío");
        }
        if (titulo.length() > 80) {
            throw new IllegalArgumentException("Título no debe exceder 80 caracteres");
        }
        this.titulo = titulo;
    }

    public Modulo agregarModulo(String nombre, int orden) {
        // Validar que no exista un módulo con ese orden
        boolean ordenExiste = modulos.stream()
                .anyMatch(m -> m.getOrden() == orden);

        if (ordenExiste) {
            throw new IllegalArgumentException(
                    "Ya existe un módulo con el orden: " + orden
            );
        }

        Modulo modulo = new Modulo(nombre, orden, this);
        modulos.add(modulo);
        return modulo;
    }

    public void eliminarModulo(Modulo modulo) {
        modulos.remove(modulo);
        modulo.setCurso(null);
    }

    public int getCantidadLecciones() {
        return modulos.stream()
                .mapToInt(m -> m.getLecciones().size())
                .sum();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
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

    public NivelCurso getNivel() {
        return nivel;
    }

    public void setNivel(NivelCurso nivel) {
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

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public List<Modulo> getModulos() {
        return new ArrayList<>(modulos);
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getUrlPortada() {
        return urlPortada;
    }

    public void setUrlPortada(String urlPortada) {
        this.urlPortada = urlPortada;
    }
}
