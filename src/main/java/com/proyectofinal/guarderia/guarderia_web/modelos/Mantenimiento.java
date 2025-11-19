package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    private String tipoEspecialidad;

}
