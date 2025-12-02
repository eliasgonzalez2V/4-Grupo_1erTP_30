package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "socioasignacion")
public class SocioAsignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_socioAsignacion")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_socio")
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "ID_garageAsignacion")
    private Garage garage;

    @ManyToOne
    @JoinColumn(name = "ID_vehiculoAsignado")
    private Vehiculo vehiculo;

    @Column(name = "fechaAsignada", nullable = false)
    private LocalDate fechaAsignada;

    public SocioAsignacion() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Garage getGarage() {
        return garage;
    }

    public void setGarage(Garage garage) {
        this.garage = garage;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public LocalDate getFechaAsignada() {
        return fechaAsignada;
    }

    public void setFechaAsignada(LocalDate fechaAsignada) {
        this.fechaAsignada = fechaAsignada;
    }
}