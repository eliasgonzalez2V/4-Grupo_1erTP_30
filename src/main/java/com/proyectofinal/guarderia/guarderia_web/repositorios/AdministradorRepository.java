package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdministradorRepository extends JpaRepository<Administrador, Integer> {

    @Query("SELECT a FROM Administrador a WHERE a.dni = :dni AND a.contrasenia = :contrasenia")
    Administrador findByDniAndContrasenia(@Param("dni") Integer dni, @Param("contrasenia") String contrasenia);

    @Query("SELECT a FROM Administrador a WHERE a.dni = :dni")
    Administrador findByDni(@Param("dni") Integer dni);
}