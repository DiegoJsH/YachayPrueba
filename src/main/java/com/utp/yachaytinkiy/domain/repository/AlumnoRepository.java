package com.utp.yachaytinkiy.domain.repository;

import com.utp.yachaytinkiy.domain.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByEmail(String email);

    boolean existsByEmail(String email);
}
