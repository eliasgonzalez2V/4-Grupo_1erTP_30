package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    
    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN v.asignacionesEmpleado ae WHERE ae.empleado.id = :empleadoId")
    List<Vehiculo> findVehiculosByEmpleadoId(@Param("empleadoId") Integer empleadoId);

    @Query("SELECT DISTINCT v FROM Vehiculo v JOIN v.asignacionesSocio sa WHERE sa.socio.id = :socioId")
    List<Vehiculo> findVehiculosBySocioId(@Param("socioId") Integer socioId);
    
    @Query("SELECT v FROM Vehiculo v WHERE v.socioPropietario.id = :socioId")
    List<Vehiculo> findBySocioPropietarioId(@Param("socioId") Integer socioId);
}