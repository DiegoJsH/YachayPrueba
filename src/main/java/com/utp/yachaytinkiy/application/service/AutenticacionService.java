package com.utp.yachaytinkiy.application.service;

import com.utp.yachaytinkiy.domain.model.*;
import com.utp.yachaytinkiy.domain.repository.AlumnoRepository;
import com.utp.yachaytinkiy.domain.repository.DocenteRepository;
import com.utp.yachaytinkiy.application.dto.LoginDTO;
import com.utp.yachaytinkiy.application.dto.UsuarioAutenticadoDTO;

import org.springframework.stereotype.Service;

@Service
public class AutenticacionService {

    private final AlumnoRepository alumnoRepository;
    private final DocenteRepository docenteRepository;

    public AutenticacionService(AlumnoRepository alumnoRepository,
            DocenteRepository docenteRepository) {
        this.alumnoRepository = alumnoRepository;
        this.docenteRepository = docenteRepository;
    }

    public UsuarioAutenticadoDTO login(LoginDTO loginDTO) {
        Usuario usuario = buscarUsuarioPorRol(loginDTO);

        if (usuario == null) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        // Verificar password (por ahora simple, luego BCrypt)
        if (!verificarPassword(loginDTO.getPassword(), usuario.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return convertirADTO(usuario);
    }

    private Usuario buscarUsuarioPorRol(LoginDTO loginDTO) {
        if ("ALUMNO".equals(loginDTO.getRol())) {
            return alumnoRepository.findByEmail(loginDTO.getEmail())
                    .orElse(null);
        } else if ("DOCENTE".equals(loginDTO.getRol())) {
            return docenteRepository.findByEmail(loginDTO.getEmail())
                    .orElse(null);
        }
        return null;
    }

    private boolean verificarPassword(String passwordPlano, String passwordEncriptado) {
        // Por ahora, verificación simple
        // Luego implementaremos BCrypt
        String passwordEncriptadoTest = "ENCRYPTED_" + passwordPlano + "_HASH";
        return passwordEncriptadoTest.equals(passwordEncriptado);
    }

    private UsuarioAutenticadoDTO convertirADTO(Usuario usuario) {
        return new UsuarioAutenticadoDTO(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNombreCompleto(),
                usuario.getRol()
        );
    }
}
