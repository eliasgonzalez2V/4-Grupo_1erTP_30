package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;

@Data
@Entity
@Table(name = "empleado")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AttributeOverride(name = "id", column = @Column(name = "ID_empleado"))
public class Empleado extends Persona {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_especialidad", nullable = false)
    @ToString.Exclude
    private Especialidad especialidad;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<EmpleadoAsignacion> asignaciones;

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }
}
