package com.proyectofinal.guarderia.guarderia_web.modelos.dto;

public class LoginForm {

    private Integer dni;
    private String contrasenia;

    // getters y setters
    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
