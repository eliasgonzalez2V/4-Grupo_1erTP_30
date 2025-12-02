package com.proyectofinal.guarderia.guarderia_web.excepciones;

public class ValidacionException extends RuntimeException {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
    
    public ValidacionException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}