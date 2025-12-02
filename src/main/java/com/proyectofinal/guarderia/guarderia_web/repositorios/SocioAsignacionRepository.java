package com.proyectofinal.guarderia.guarderia_web.repositorios;

import com.proyectofinal.guarderia.guarderia_web.modelos.SocioAsignacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SocioAsignacionRepository extends JpaRepository<SocioAsignacion, Integer> {

    List<SocioAsignacion> findBySocioId(Integer socioId);

    @Query("SELECT sa FROM SocioAsignacion sa "
            + "LEFT JOIN FETCH sa.socio "
            + "LEFT JOIN FETCH sa.garage g "
            + "LEFT JOIN FETCH g.zona "
            + "LEFT JOIN FETCH sa.vehiculo "
            + "WHERE sa.socio.id = :socioId")
    List<SocioAsignacion> findBySocioIdWithJoins(@Param("socioId") Integer socioId);

    @Query("SELECT sa FROM SocioAsignacion sa "
            + "LEFT JOIN FETCH sa.socio "
            + "LEFT JOIN FETCH sa.garage g "
            + "LEFT JOIN FETCH g.zona "
            + "LEFT JOIN FETCH sa.vehiculo")
    List<SocioAsignacion> findAllWithJoins();
}