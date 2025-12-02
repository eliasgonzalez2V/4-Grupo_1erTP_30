package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "mantenimientocontratado")
public class MantenimientoContratado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_orden")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_mantenimiento")
    private Mantenimiento mantenimiento;

    @ManyToOne
    @JoinColumn(name = "ID_vehiculo")
    private Vehiculo vehiculo;

    public MantenimientoContratado() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mantenimiento getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}