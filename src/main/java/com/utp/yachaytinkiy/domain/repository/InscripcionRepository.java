package com.utp.yachaytinkiy.domain.repository;

import com.utp.yachaytinkiy.domain.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByAlumnoId(Long alumnoId);

    List<Inscripcion> findByCursoId(Long cursoId);

    Optional<Inscripcion> findByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);

    boolean existsByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);
}
