package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import org.springframework.data.repository.CrudRepository;

public interface SocioRepository extends CrudRepository<Socio, Integer> {
    
    
    Socio findByDniAndContrasenia(Integer dni, String contrasenia);
}