package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "administrador")
@EqualsAndHashCode(callSuper = true)
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "ID_administrador")),
    @AttributeOverride(name = "dni", column = @Column(name = "dni")),
    @AttributeOverride(name = "nombre", column = @Column(name = "nombre")),
    @AttributeOverride(name = "direccion", column = @Column(name = "direccion")),
    @AttributeOverride(name = "telefono", column = @Column(name = "telefono")),
    @AttributeOverride(name = "contrasenia", column = @Column(name = "contrasenia"))
})
public class Administrador extends Persona {

}