package com.tesis.proyect.app.utils;

public enum EstadoEntrevista {
    PENDIENTE("PENDIENTE"),
    EN_PROGRESO("EN_PROGRESO"),
    COMPLETADA("COMPLETADA"),
    CANCELADA("CANCELADA");

    private final String estado;

    EstadoEntrevista(String estado) {
        this.estado = estado;
    }
}
