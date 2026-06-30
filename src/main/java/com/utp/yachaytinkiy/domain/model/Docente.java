package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "docentes")
public class Docente extends Usuario {

    @NotBlank(message = "Especialidad es obligatoria")
    @Size(min = 3, max = 100)
    private String especialidad;

    @NotBlank(message = "Grado académico es obligatorio")
    private String gradoAcademico;

    @Size(max = 300, message = "Biografía no debe exceder 300 caracteres")
    @Column(length = 300)
    private String biografia;

    @OneToMany(mappedBy = "docente", cascade = CascadeType.ALL)
    private List<Curso> cursosCreados;

    // Constructor para JPA
    protected Docente() {
        super();
        this.cursosCreados = new ArrayList<>();
    }

    public Docente(String email, String password, String nombreCompleto,
            String especialidad, String gradoAcademico, String biografia) {
        super(email, password, nombreCompleto);
        this.setEspecialidad(especialidad);
        this.gradoAcademico = gradoAcademico;
        this.biografia = biografia;
        this.cursosCreados = new ArrayList<>();
        this.setRol(RolUsuario.DOCENTE);
    }

    public void setEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.trim().isEmpty()) {
            throw new IllegalArgumentException("Especialidad no puede estar vacia");
        }
        this.especialidad = especialidad;
    }
    
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    
    public Curso crearCurso(String titulo, String descripcion,
            String categoria, NivelCurso nivel) {
        Curso curso = new Curso(titulo, descripcion, categoria, nivel, this);
        cursosCreados.add(curso);
        return curso;
    }

    // Getters
    public String getEspecialidad() {
        return especialidad;
    }

    public String getGradoAcademico() {
        return gradoAcademico;
    }

    public String getBiografia() {
        return biografia;
    }

    public List<Curso> getCursosCreados() {
        return new ArrayList<>(cursosCreados);
    }
}
