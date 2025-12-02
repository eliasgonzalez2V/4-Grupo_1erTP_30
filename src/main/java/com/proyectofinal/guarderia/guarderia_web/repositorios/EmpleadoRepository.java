package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    @Query("SELECT e FROM Empleado e WHERE e.dni = :dni AND e.contrasenia = :contrasenia")
    Empleado findByDniAndContrasenia(@Param("dni") Integer dni, @Param("contrasenia") String contrasenia);

    @Query("SELECT e FROM Empleado e WHERE e.dni = :dni")
    Empleado findByDni(@Param("dni") Integer dni);
}