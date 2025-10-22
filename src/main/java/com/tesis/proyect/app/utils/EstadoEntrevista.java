package com.tesis.proyect.app.utils;

public enum EstadoEntrevista {
    PENDIENTE("PENDIENTE"),
    COMPLETADA("COMPLETADA"),
    NO_ASIGNADA("NO_ASIGNADA");

    private final String estado;

    EstadoEntrevista(String estado) {
        this.estado = estado;
    }
}
