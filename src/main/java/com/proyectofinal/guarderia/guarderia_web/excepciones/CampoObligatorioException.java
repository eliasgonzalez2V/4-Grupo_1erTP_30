package com.proyectofinal.guarderia.guarderia_web.excepciones;

public class CampoObligatorioException extends RuntimeException {
    private String campo;
    
    public CampoObligatorioException(String campo) {
        super("El campo '" + campo + "' es obligatorio y no puede estar vacío");
        this.campo = campo;
    }
    
    public String getCampo() {
        return campo;
    }
}