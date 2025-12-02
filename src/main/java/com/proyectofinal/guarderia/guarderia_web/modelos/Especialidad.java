package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_especialidad")
    private Integer id;

    @Column(name = "tipoEspecialidad", nullable = false, length = 100)
    private String tipoEspecialidad;

    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Empleado> empleados;

    public Especialidad() {
    }

    public Especialidad(Integer id, String tipoEspecialidad, List<Empleado> empleados) {
        this.id = id;
        this.tipoEspecialidad = tipoEspecialidad;
        this.empleados = empleados;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoEspecialidad() {
        return tipoEspecialidad;
    }

    public void setTipoEspecialidad(String tipoEspecialidad) {
        this.tipoEspecialidad = tipoEspecialidad;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }

    @Override
    public String toString() {
        return "Especialidad{" +
                "id=" + id +
                ", tipoEspecialidad='" + tipoEspecialidad + '\'' +
                '}';
    }
}