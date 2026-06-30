package com.utp.yachaytinkiy.domain.repository;

import com.utp.yachaytinkiy.domain.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    Optional<Docente> findByEmail(String email);

    boolean existsByEmail(String email);
}
