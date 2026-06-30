package com.utp.yachaytinkiy.application.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * DTO para crear un curso completo con módulos y lecciones.
 */
public class CrearCursoDTO {

    @NotBlank(message = "Título es obligatorio")
    @Size(max = 80, message = "Título no debe exceder 80 caracteres")
    private String titulo;

    @NotBlank(message = "Descripción es obligatoria")
    @Size(max = 500, message = "Descripción no debe exceder 500 caracteres")
    private String descripcion;

    @NotBlank(message = "Categoría es obligatoria")
    private String categoria;

    @NotBlank(message = "Nivel es obligatorio")
    private String nivel; // "PRINCIPIANTE", "INTERMEDIO", "AVANZADO", "TODOS"

    @Min(value = 1, message = "Duración mínima es 1 hora")
    @Max(value = 200, message = "Duración máxima es 200 horas")
    private Integer duracion;

    @DecimalMin(value = "0.0", message = "Precio no puede ser negativo")
    private BigDecimal precio;

    @NotNull(message = "ID del docente es obligatorio")
    private Long docenteId;

    @Valid
    private List<CrearModuloDTO> modulos = new ArrayList<>();

    // Constructores
    public CrearCursoDTO() {
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Long getDocenteId() {
        return docenteId;
    }

    public void setDocenteId(Long docenteId) {
        this.docenteId = docenteId;
    }

    public List<CrearModuloDTO> getModulos() {
        return modulos;
    }

    public void setModulos(List<CrearModuloDTO> modulos) {
        this.modulos = modulos;
    }
}
