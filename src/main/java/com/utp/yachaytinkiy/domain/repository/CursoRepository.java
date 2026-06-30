package com.utp.yachaytinkiy.domain.repository;

import com.utp.yachaytinkiy.domain.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    List<Curso> findByDocenteId(Long docenteId);

    List<Curso> findByCategoria(String categoria);

    List<Curso> findByTituloContainingIgnoreCase(String titulo);
}
