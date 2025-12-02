package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "socio")
@PrimaryKeyJoinColumn(name = "ID_persona")
public class Socio extends Persona {

    @Column(name = "fechaIngreso")
    private LocalDate fechaIngreso;

    public Socio() {
        super();
    }

    @PrePersist
    @PreUpdate
    public void prePersist() {
        this.setTipo_persona("socio");
        if (this.fechaIngreso == null) {
            this.fechaIngreso = LocalDate.now();
        }
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}