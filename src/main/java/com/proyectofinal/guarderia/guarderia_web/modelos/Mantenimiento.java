package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "mantenimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_mantenimiento")
    private Integer id;

    @Column(name = "tipoMantenimiento", nullable = false, length = 50)
    private String tipoMantenimiento;

    @Column(name = "tipoEspecialidad", nullable = false, length = 50)
    private String especialidadMantenimiento;

    @OneToMany(mappedBy = "mantenimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MantenimientoContratado> mantenimientosContratados;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public String getEspecialidadMantenimiento() {
        return especialidadMantenimiento;
    }

    public void setEspecialidadMantenimiento(String especialidadMantenimiento) {
        this.especialidadMantenimiento = especialidadMantenimiento;
    }

    public List<MantenimientoContratado> getMantenimientosContratados() {
        return mantenimientosContratados;
    }

    public void setMantenimientosContratados(List<MantenimientoContratado> mantenimientosContratados) {
        this.mantenimientosContratados = mantenimientosContratados;
    }
}
