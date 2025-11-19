package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Administrador;
import org.springframework.data.repository.CrudRepository;

public interface AdministradorRepository extends CrudRepository<Administrador, Integer> {
    
    
    Administrador findByDniAndContrasenia(Integer dni, String contrasenia);
}