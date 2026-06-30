package com.utp.yachaytinkiy.application.dto;

import java.util.List;

public class ModuloProgresoDTO {

    private Long moduloId;
    private String nombre;
    private int orden;
    private List<LeccionProgresoDTO> lecciones;

    // Constructor vacío
    public ModuloProgresoDTO() {
    }

    // Constructor completo
    public ModuloProgresoDTO(Long moduloId, String nombre, int orden,
            List<LeccionProgresoDTO> lecciones) {
        this.moduloId = moduloId;
        this.nombre = nombre;
        this.orden = orden;
        this.lecciones = lecciones;
    }

    // Getters y Setters
    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }

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

    public List<LeccionProgresoDTO> getLecciones() {
        return lecciones;
    }

    public void setLecciones(List<LeccionProgresoDTO> lecciones) {
        this.lecciones = lecciones;
    }
}
