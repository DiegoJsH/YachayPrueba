package com.utp.yachaytinkiy.domain.model;

public enum TipoContenido {
    VIDEO("Video"),
    TEXTO("Texto"),
    PDF("PDF"),
    QUIZ("Quiz");

    private final String displayName;

    TipoContenido(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
