package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modulos")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre del módulo es obligatorio")
    @Size(max = 100, message = "Nombre no debe exceder 100 caracteres")
    private String nombre;

    @Min(value = 1, message = "Orden debe ser mayor a 0")
    private int orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<Leccion> lecciones;

    // Constructor para JPA
    protected Modulo() {
        this.lecciones = new ArrayList<>();
    }

    // Constructor principal
    public Modulo(String nombre, int orden, Curso curso) {
        this();
        this.setNombre(nombre);
        this.setOrden(orden);
        this.curso = curso;
    }

    // Métodos de validación
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre del módulo no puede estar vacío");
        }
        this.nombre = nombre;
    }

    public void setOrden(int orden) {
        if (orden < 1) {
            throw new IllegalArgumentException("Orden debe ser mayor a 0");
        }
        this.orden = orden;
    }

    // Métodos de negocio
    public Leccion agregarLeccion(String titulo, TipoContenido tipo, int orden) {
        // Validar que no exista una lección con ese orden
        boolean ordenExiste = lecciones.stream()
                .anyMatch(l -> l.getOrden() == orden);

        if (ordenExiste) {
            throw new IllegalArgumentException(
                    "Ya existe una lección con el orden: " + orden
            );
        }

        Leccion leccion = new Leccion(titulo, tipo, orden, this);
        lecciones.add(leccion);
        return leccion;
    }

    public void eliminarLeccion(Leccion leccion) {
        lecciones.remove(leccion);
        leccion.setModulo(null);
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getOrden() {
        return orden;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Leccion> getLecciones() {
        return new ArrayList<>(lecciones);
    }
}
