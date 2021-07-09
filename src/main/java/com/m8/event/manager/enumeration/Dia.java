package com.m8.event.manager.enumeration;

public enum Dia {
    
    LUNES ("lunes"),
    MARTES ("martes"),
    MIERCOLES ("miercoles"),
    JUEVES ("jueves"),
    VIERNES ("viernes"),
    SABADO ("sabado"),
    DOMINGO ("domingo");
    
    Dia (String nombreDia){
        this.nombreDia = nombreDia;
    }    
    
    private final String nombreDia;

    public static Dia get(String nombre){
        for (Dia dia : values()) {
            if (dia.nombreDia.equalsIgnoreCase(nombre)) {
                return dia;
            }
        }
        return null;
    }
}
