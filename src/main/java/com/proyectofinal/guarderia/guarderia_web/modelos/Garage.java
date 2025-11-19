package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;

@Data
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
    @ToString.Exclude
    private Zona zona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_vehiculoAsignado")
    @ToString.Exclude
    private Vehiculo vehiculoAsignado;
    
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<SocioAsignacion> asignacionesSocio;

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