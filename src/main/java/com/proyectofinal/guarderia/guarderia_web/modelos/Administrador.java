package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "administrador")
@PrimaryKeyJoinColumn(name = "ID_persona")
public class Administrador extends Persona {

    public Administrador() {
        super();
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.setTipo_persona("administrador");
    }
}