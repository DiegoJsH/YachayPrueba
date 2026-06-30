package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alumnos")
public class Alumno extends Usuario {

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL)
    private List<Inscripcion> cursosInscritos;

    @OneToMany(mappedBy = "alumno")
    private List<Certificado> certificados;

    // Constructor para JPA
    protected Alumno() {
        super();
        this.cursosInscritos = new ArrayList<>();
        this.certificados = new ArrayList<>();
    }

    public Alumno(String email, String password, String nombreCompleto) {
        super(email, password, nombreCompleto);
        this.cursosInscritos = new ArrayList<>();
        this.certificados = new ArrayList<>();
        this.setRol(RolUsuario.ALUMNO);
    }

    public void inscribirseACurso(Curso curso) {
        // Verificar si ya está inscrito
        boolean yaInscrito = cursosInscritos.stream()
                .anyMatch(i -> i.getCurso().equals(curso));

        if (yaInscrito) {
            throw new IllegalStateException("Ya estas inscrito en este curso");
        }

        Inscripcion inscripcion = new Inscripcion(this, curso);
        cursosInscritos.add(inscripcion);
    }

    public List<Inscripcion> getCursosInscritos() {
        return new ArrayList<>(cursosInscritos);
    }

    public List<Certificado> getCertificados() {
        return new ArrayList<>(certificados);
    }
}
