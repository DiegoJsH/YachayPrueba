package com.utp.yachaytinkiy.domain.model;

public enum NivelCurso {
    PRINCIPIANTE("Principiante"),
    INTERMEDIO("Intermedio"),
    AVANZADO("Avanzado"),
    TODOS("Todos los niveles");

    private final String displayName;

    NivelCurso(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
