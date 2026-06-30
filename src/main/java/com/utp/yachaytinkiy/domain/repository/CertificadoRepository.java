package com.utp.yachaytinkiy.domain.repository;

import com.utp.yachaytinkiy.domain.model.Certificado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Repositorio para gestión de certificados.
 */
@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    /**
     * Busca certificados por alumno.
     * @param alumnoId
     * @return 
     */
    List<Certificado> findByAlumnoId(Long alumnoId);

    /**
     * Busca certificado por alumno y curso.
     * @param alumnoId
     * @param cursoId
     * @return 
     */
    Optional<Certificado> findByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);

    /**
     * Verifica si existe certificado para un alumno en un curso.
     * @param alumnoId
     * @param cursoId
     * @return 
     */
    boolean existsByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);

    /**
     * Busca certificado por código de verificación.
     * @param codigoVerificacion
     * @return 
     */
    Optional<Certificado> findByCodigoVerificacion(String codigoVerificacion);
}
