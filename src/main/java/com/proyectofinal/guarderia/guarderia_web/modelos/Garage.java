package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "garage")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_garage")
    private Integer id;

    private Integer contadorLuz;
    private LocalDate fechaInicioRenta;
    private LocalDate fechaFinRenta;
    private LocalDate fechaCompra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_zona")
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_vehiculoAsignado")
    private Vehiculo vehiculoAsignado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_socioPropietario")
    private Socio socioPropietario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_socioRentado")
    private Socio socioRentado;
    
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocioAsignacion> asignacionesSocio = new ArrayList<>();

    public Garage() {
        this.asignacionesSocio = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContadorLuz() {
        return contadorLuz;
    }

    public void setContadorLuz(Integer contadorLuz) {
        this.contadorLuz = contadorLuz;
    }

    public LocalDate getFechaInicioRenta() {
        return fechaInicioRenta;
    }

    public void setFechaInicioRenta(LocalDate fechaInicioRenta) {
        this.fechaInicioRenta = fechaInicioRenta;
    }

    public LocalDate getFechaFinRenta() {
        return fechaFinRenta;
    }

    public void setFechaFinRenta(LocalDate fechaFinRenta) {
        this.fechaFinRenta = fechaFinRenta;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Vehiculo getVehiculoAsignado() {
        return vehiculoAsignado;
    }

    public void setVehiculoAsignado(Vehiculo vehiculoAsignado) {
        this.vehiculoAsignado = vehiculoAsignado;
    }

    public Socio getSocioPropietario() {
        return socioPropietario;
    }

    public void setSocioPropietario(Socio socioPropietario) {
        this.socioPropietario = socioPropietario;
    }

    public Socio getSocioRentado() {
        return socioRentado;
    }

    public void setSocioRentado(Socio socioRentado) {
        this.socioRentado = socioRentado;
    }

    public List<SocioAsignacion> getAsignacionesSocio() {
        if (asignacionesSocio == null) {
            asignacionesSocio = new ArrayList<>();
        }
        return asignacionesSocio;
    }

    public void setAsignacionesSocio(List<SocioAsignacion> asignacionesSocio) {
        if (asignacionesSocio == null) {
            this.asignacionesSocio.clear();
        } else {
            this.asignacionesSocio = asignacionesSocio;
        }
    }

    public boolean esComprado() {
        return socioPropietario != null && fechaCompra != null;
    }

    public boolean esRentado() {
        return socioRentado != null && fechaInicioRenta != null && fechaFinRenta != null;
    }

    @PrePersist
    @PreUpdate
    public void initializeCollections() {
        if (this.asignacionesSocio == null) {
            this.asignacionesSocio = new ArrayList<>();
        }
    }

    @Override
    public String toString() {
        return "Garage{" +
                "id=" + id +
                ", contadorLuz=" + contadorLuz +
                ", fechaInicioRenta=" + fechaInicioRenta +
                ", fechaFinRenta=" + fechaFinRenta +
                ", fechaCompra=" + fechaCompra +
                '}';
    }
}