package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Mantenimiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    @Query("SELECT DISTINCT m FROM Mantenimiento m JOIN EmpleadoAsignacion ea ON m.id = ea.mantenimiento.id WHERE ea.empleado.id = :empleadoId")
    List<Mantenimiento> findMantenimientosByEmpleadoId(@Param("empleadoId") Integer empleadoId);

    @Query("SELECT DISTINCT m FROM Mantenimiento m " +
           "JOIN MantenimientoContratado mc ON m.id = mc.mantenimiento.id " +
           "JOIN Vehiculo v ON mc.vehiculo.id = v.id " +
           "JOIN SocioAsignacion sa ON v.id = sa.vehiculo.id " +
           "WHERE sa.socio.id = :socioId")
    List<Mantenimiento> findMantenimientosBySocioId(@Param("socioId") Integer socioId);
}