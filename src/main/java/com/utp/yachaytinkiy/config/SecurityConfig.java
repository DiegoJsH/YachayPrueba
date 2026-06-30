package com.utp.yachaytinkiy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para la aplicación web.
 *
 * Esta configuración implementa: - Autenticación basada en sesiones (cookies) -
 * Form login tradicional para páginas web - Autorización por roles (ALUMNO,
 * DOCENTE) - Integración con CustomUserDetailsService
 *
 * NO incluye: - API REST - Autenticación JWT - Stateless authentication
 *
 * @author YachayTinkiy Team
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configura la cadena de filtros de seguridad HTTP.
     *
     * Define: - Qué rutas son públicas y cuáles requieren autenticación - Qué
     * roles tienen acceso a qué rutas - Configuración de login y logout -
     * Manejo de sesiones
     *
     * @param http Objeto de configuración de seguridad HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception si hay error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ===== AUTORIZACIÓN DE RUTAS =====
                .authorizeHttpRequests(auth -> auth
                // ----- RUTAS PÚBLICAS (sin autenticación) -----
                .requestMatchers(
                        "/", // Página principal
                        "/login", // Página de login
                        "/registro/**", // Registro alumno/docente
                        "/catalogo", // Catálogo de cursos
                        "/cursos/{id:[0-9]+}", // Detalle de curso (regex: solo números)
                        "/error" // Página de error
                ).permitAll()
                // ----- RECURSOS ESTÁTICOS -----
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/favicon.ico"
                ).permitAll()
                // ----- RUTAS PROTEGIDAS - ALUMNO -----
                .requestMatchers(
                        "/perfil/alumno", // Perfil del alumno
                        "/perfil/progreso/**", // Progreso en cursos
                        "/cursos/*/inscribirse" // Inscripción a cursos
                ).hasRole("ALUMNO")
                // ----- RUTAS PROTEGIDAS - DOCENTE -----
                .requestMatchers(
                        "/perfil/docente", // Perfil del docente
                        "/cursos/crear", // Crear curso
                        "/cursos/*/editar" // Editar curso
                ).hasRole("DOCENTE")
                // ----- CUALQUIER OTRA RUTA REQUIERE AUTENTICACIÓN -----
                .anyRequest().authenticated()
                )
                // ===== CONFIGURACIÓN DE LOGIN (FORM-BASED) =====
                .formLogin(form -> form
                .loginPage("/login") // URL de la página de login
                .loginProcessingUrl("/login") // URL donde se procesa el login
                .usernameParameter("username") // Nombre del campo email en el form
                .passwordParameter("password") // Nombre del campo password en el form
                .defaultSuccessUrl("/catalogo", true) // Redirección después de login exitoso
                .failureUrl("/login?error=true") // Redirección si falla el login
                .permitAll() // Permitir acceso sin autenticación
                )
                // ===== CONFIGURACIÓN DE LOGOUT =====
                .logout(logout -> logout
                .logoutUrl("/logout") // URL para cerrar sesión
                .logoutSuccessUrl("/?logout=true") // Redirección después de logout
                .invalidateHttpSession(true) // Invalidar la sesión HTTP
                .deleteCookies("JSESSIONID") // Borrar cookie de sesión
                .permitAll() // Permitir acceso sin autenticación
                )
                // ===== PROTECCIÓN CSRF =====
                // Habilitada por defecto para proteger formularios web
                // Spring Security genera automáticamente tokens CSRF en formularios Thymeleaf
                .csrf(Customizer.withDefaults())
                // ===== GESTIÓN DE SESIONES =====
                .sessionManagement(session -> session
                .maximumSessions(1) // Máximo 1 sesión por usuario
                .maxSessionsPreventsLogin(false) // Permite login, cierra sesión anterior
                );

        return http.build();
    }

    /**
     * Bean de AuthenticationManager.
     *
     * Necesario para autenticación programática en servicios (si se requiere).
     * Spring Security lo usa internamente para procesar el login.
     *
     * NOTA: En Spring Security 6.x, el DaoAuthenticationProvider se configura
     * automáticamente al detectar los beans de UserDetailsService y
     * PasswordEncoder. Ya no es necesario crearlo manualmente.
     *
     * @param authConfig Configuración de autenticación de Spring
     * @return AuthenticationManager
     * @throws Exception si hay error al obtener el manager
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configuración del encoder de contraseñas con BCrypt.
     *
     * BCrypt: - Algoritmo de hashing unidireccional - Incluye salt automático -
     * Resistente a ataques de fuerza bruta - Estándar de la industria para
     * contraseñas
     *
     * @return PasswordEncoder con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
