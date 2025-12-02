package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GarageRepository extends JpaRepository<Garage, Integer> {

    @Query("SELECT g FROM Garage g LEFT JOIN FETCH g.zona WHERE g.socioRentado.id = :socioId AND g.fechaInicioRenta IS NOT NULL")
    List<Garage> findBySocioRentadoId(@Param("socioId") Integer socioId);

    @Query("SELECT g FROM Garage g LEFT JOIN FETCH g.zona WHERE g.socioPropietario.id = :socioId AND g.fechaCompra IS NOT NULL")
    List<Garage> findBySocioPropietarioId(@Param("socioId") Integer socioId);

    @Query("SELECT g FROM Garage g WHERE g.vehiculoAsignado.id = :vehiculoId")
    List<Garage> findByVehiculoAsignadoId(@Param("vehiculoId") Integer vehiculoId);

    @Query("SELECT g FROM Garage g WHERE g.vehiculoAsignado.id = :vehiculoId")
    List<Garage> findByVehiculoAsignado(@Param("vehiculoId") Integer vehiculoId);
}
