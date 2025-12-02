package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.MantenimientoContratado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MantenimientoContratadoRepository extends JpaRepository<MantenimientoContratado, Integer> {
    
    @Query("SELECT mc FROM MantenimientoContratado mc JOIN FETCH mc.vehiculo JOIN FETCH mc.mantenimiento " +
           "WHERE mc.vehiculo.id IN " +
           "(SELECT sa.vehiculo.id FROM SocioAsignacion sa WHERE sa.socio.id = :socioId)")
    List<MantenimientoContratado> findBySocioId(@Param("socioId") Integer socioId);
}