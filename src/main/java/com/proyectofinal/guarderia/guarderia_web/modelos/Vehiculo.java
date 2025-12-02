package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_vehiculo")
    private Integer id;

    private String matricula;
    private String marca;
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "ID_mantenimiento")
    private Mantenimiento mantenimiento;

    @ManyToOne
    @JoinColumn(name = "ID_socioPropietario")
    private Socio socioPropietario;

    @OneToMany(mappedBy = "vehiculo")
    private List<SocioAsignacion> asignacionesSocio = new ArrayList<>();

    @OneToMany(mappedBy = "vehiculo")
    private List<EmpleadoAsignacion> asignacionesEmpleado = new ArrayList<>();

    @OneToMany(mappedBy = "vehiculo")
    private List<MantenimientoContratado> mantenimientosContratados = new ArrayList<>();

    @Transient
    private Garage garageAsignado;

    public Vehiculo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Mantenimiento getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Mantenimiento mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    public Socio getSocioPropietario() {
        return socioPropietario;
    }

    public void setSocioPropietario(Socio socioPropietario) {
        this.socioPropietario = socioPropietario;
    }

    public List<SocioAsignacion> getAsignacionesSocio() {
        return asignacionesSocio;
    }

    public void setAsignacionesSocio(List<SocioAsignacion> asignacionesSocio) {
        this.asignacionesSocio = asignacionesSocio;
    }

    public List<EmpleadoAsignacion> getAsignacionesEmpleado() {
        return asignacionesEmpleado;
    }

    public void setAsignacionesEmpleado(List<EmpleadoAsignacion> asignacionesEmpleado) {
        this.asignacionesEmpleado = asignacionesEmpleado;
    }

    public List<MantenimientoContratado> getMantenimientosContratados() {
        return mantenimientosContratados;
    }

    public void setMantenimientosContratados(List<MantenimientoContratado> mantenimientosContratados) {
        this.mantenimientosContratados = mantenimientosContratados;
    }

    public Garage getGarageAsignado() {
        return garageAsignado;
    }

    public void setGarageAsignado(Garage garageAsignado) {
        this.garageAsignado = garageAsignado;
    }
}