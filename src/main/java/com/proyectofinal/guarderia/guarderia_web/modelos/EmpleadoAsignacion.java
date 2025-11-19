package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "empleadoasignacion")
public class EmpleadoAsignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_orden") 
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "ID_empAsignacion") 
    private Empleado empleado;

    @ManyToOne
    @JoinColumn(name = "ID_vehiculoAsignado") 
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "ID_mantenimientoAsignado") 
    private Mantenimiento mantenimiento;

}