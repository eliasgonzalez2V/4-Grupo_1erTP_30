package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.EmpleadoAsignacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoAsignacionRepository extends JpaRepository<EmpleadoAsignacion, Integer> {
    List<EmpleadoAsignacion> findByEmpleadoId(Integer empleadoId);
}