package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "zona")
@Data 
public class Zona {

    @Id
    @Column(name = "ID_zona")
    private String id;

    private String tipoVehiculo;
    private Double profundidadGarages;
    private Double anchoGarages;
    
    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Garage> garages;

    @Override
    public String toString() {
        return "Zona{" +
                "id='" + id + '\'' +
                ", tipoVehiculo='" + tipoVehiculo + '\'' +
                ", profundidadGarages=" + profundidadGarages +
                ", anchoGarages=" + anchoGarages +
                '}';
    }
}