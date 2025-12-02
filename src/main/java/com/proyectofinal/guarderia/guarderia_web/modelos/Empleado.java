package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
@PrimaryKeyJoinColumn(name = "ID_persona")
public class Empleado extends Persona {

    @ManyToOne
    @JoinColumn(name = "ID_especialidad")
    private Especialidad especialidad;

    public Empleado() {
        super();
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.setTipo_persona("empleado");
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
}