package com.utp.yachaytinkiy.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import com.utp.yachaytinkiy.domain.validator.*;

/**
 * Entidad base abstracta para usuarios del sistema.
 *
 * Utiliza herencia de tipo JOINED, lo que significa: - Esta tabla contiene los
 * campos comunes de todos los usuarios - Las tablas hijas (Alumno, Docente)
 * extienden esta tabla - Se hace un JOIN cuando se consulta una entidad hija
 *
 * Campos comunes: - id: Identificador único - email: Email del usuario (usado
 * como username en Spring Security) - password: Contraseña encriptada con
 * BCrypt - nombreCompleto: Nombre completo del usuario - rol: Tipo de usuario
 * (ALUMNO o DOCENTE) - fechaRegistro: Fecha de creación de la cuenta
 *
 * @author YachayTinkiy Team
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuarios")
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email debe ser válido")
    @NotBlank(message = "Email es obligatorio")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password es obligatorio")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Nombre completo es obligatorio")
    @Size(min = 3, max = 100)
    private String nombreCompleto;

    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Constructor protegido para JPA. Inicializa la fecha de registro
     * automáticamente.
     */
    protected Usuario() {
        this.fechaRegistro = LocalDateTime.now();
    }

    /**
     * Constructor principal con validaciones.
     *
     * IMPORTANTE: La contraseña debe venir YA ENCRIPTADA desde el servicio.
     * Este constructor NO encripta la contraseña automáticamente.
     *
     * @param email Email del usuario
     * @param passwordEncriptada Contraseña YA encriptada con BCrypt
     * @param nombreCompleto Nombre completo del usuario
     */
    public Usuario(String email, String passwordEncriptada, String nombreCompleto) {
        this();
        this.setEmail(email);
        this.password = passwordEncriptada;  // ← Asignar directamente, ya viene encriptada
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Valida y establece el email del usuario.
     *
     * Utiliza EmailValidator para verificar formato correcto.
     *
     * @param email Email a validar
     * @throws IllegalArgumentException si el email es inválido
     */
    public void setEmail(String email) {
        if (!EmailValidator.isValid(email)) {
            throw new IllegalArgumentException("Email inválido: " + email);
        }
        this.email = email;
    }

    /**
     * Establece el rol del usuario.
     *
     * Este método es protected porque solo debe ser llamado desde las clases
     * hijas (Alumno, Docente).
     *
     * @param rol Rol del usuario (ALUMNO o DOCENTE)
     */
    protected void setRol(RolUsuario rol) {
        this.rol = rol;
    }
    /**
     * Establece la contraseña del usuario.
     *
     * IMPORTANTE: Solo usar con contraseñas YA encriptadas. No encripta
     * automáticamente.
     *
     * @param password Contraseña encriptada con BCrypt
     */
    public void setPassword(String password) {
        this.password = password;
    }
    // ===================================================
    // GETTERS
    // ===================================================
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        this.nombreCompleto = nombreCompleto.trim();
    }
    
    public RolUsuario getRol() {
        return rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
}
