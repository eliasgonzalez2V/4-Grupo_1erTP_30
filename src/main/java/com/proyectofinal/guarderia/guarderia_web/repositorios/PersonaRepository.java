package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    Optional<Persona> findByDniAndContrasenia(Integer dni, String contrasenia);
}