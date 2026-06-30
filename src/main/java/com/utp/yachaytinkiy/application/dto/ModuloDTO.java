package com.utp.yachaytinkiy.application.dto;

import java.util.List;

public class ModuloDTO {

    private Long id;
    private String nombre;
    private int orden;
    private List<LeccionDTO> lecciones;

    public ModuloDTO() {
    }

    public ModuloDTO(Long id, String nombre, int orden, List<LeccionDTO> lecciones) {
        this.id = id;
        this.nombre = nombre;
        this.orden = orden;
        this.lecciones = lecciones;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<LeccionDTO> getLecciones() {
        return lecciones;
    }

    public void setLecciones(List<LeccionDTO> lecciones) {
        this.lecciones = lecciones;
    }
}
