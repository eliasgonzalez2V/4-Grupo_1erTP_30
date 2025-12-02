package com.proyectofinal.guarderia.guarderia_web.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zona")
public class Zona {

    @Id
    @Column(name = "ID_zona")
    @Size(min = 1, max = 1, message = "El ID de zona debe ser exactamente 1 carácter")
    @Pattern(regexp = "[A-Za-z]", message = "El ID de zona debe ser una letra de la A a la Z")
    private String id;

    private String tipoVehiculo;
    private Double profundidadGarages;
    private Double anchoGarages;
    
    @OneToMany(mappedBy = "zona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Garage> garages = new ArrayList<>();

    public Zona() {
        this.garages = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Double getProfundidadGarages() {
        return profundidadGarages;
    }

    public void setProfundidadGarages(Double profundidadGarages) {
        this.profundidadGarages = profundidadGarages;
    }

    public Double getAnchoGarages() {
        return anchoGarages;
    }

    public void setAnchoGarages(Double anchoGarages) {
        this.anchoGarages = anchoGarages;
    }

    public List<Garage> getGarages() {
        if (garages == null) {
            garages = new ArrayList<>();
        }
        return garages;
    }

    public void setGarages(List<Garage> garages) {
        if (garages == null) {
            this.garages.clear();
        } else {
            this.garages = garages;
        }
    }

    @PrePersist
    @PreUpdate
    public void initializeCollections() {
        if (this.garages == null) {
            this.garages = new ArrayList<>();
        }
    }

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