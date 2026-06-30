package com.utp.yachaytinkiy.application.dto;

import jakarta.validation.constraints.*;
import java.util.List;
import java.util.ArrayList;

/**
 * DTO para crear un módulo con sus lecciones.
 */
public class CrearModuloDTO {

    @NotBlank(message = "Nombre del módulo es obligatorio")
    @Size(max = 100, message = "Nombre no debe exceder 100 caracteres")
    private String nombre;

    @Min(value = 1, message = "Orden debe ser mayor a 0")
    private int orden;

    private List<CrearLeccionDTO> lecciones = new ArrayList<>();

    // Constructores
    public CrearModuloDTO() {
    }

    public CrearModuloDTO(String nombre, int orden) {
        this.nombre = nombre;
        this.orden = orden;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public List<CrearLeccionDTO> getLecciones() {
        return lecciones;
    }

    public void setLecciones(List<CrearLeccionDTO> lecciones) {
        this.lecciones = lecciones;
    }
}
