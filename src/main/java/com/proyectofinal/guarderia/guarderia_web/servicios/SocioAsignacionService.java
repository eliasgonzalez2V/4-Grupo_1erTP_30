package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.SocioAsignacion;
import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import com.proyectofinal.guarderia.guarderia_web.repositorios.SocioAsignacionRepository;
import com.proyectofinal.guarderia.guarderia_web.repositorios.GarageRepository;
import com.proyectofinal.guarderia.guarderia_web.repositorios.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SocioAsignacionService {

    @Autowired
    private SocioAsignacionRepository socioAsignacionRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public List<SocioAsignacion> listarTodos() {
        return socioAsignacionRepository.findAll();
    }

    public List<SocioAsignacion> listarTodosWithJoins() {
        return socioAsignacionRepository.findAllWithJoins();
    }

    public SocioAsignacion buscarPorId(Integer id) {
        Optional<SocioAsignacion> socioAsignacion = socioAsignacionRepository.findById(id);
        return socioAsignacion.orElse(null);
    }

    public SocioAsignacion guardar(SocioAsignacion socioAsignacion) {
        return socioAsignacionRepository.save(socioAsignacion);
    }

    public void eliminar(Integer id) {
        socioAsignacionRepository.deleteById(id);
    }

    public List<SocioAsignacion> buscarPorSocioId(Integer socioId) {
        return socioAsignacionRepository.findBySocioId(socioId);
    }

    public List<SocioAsignacion> buscarPorSocioIdWithJoins(Integer socioId) {
        return socioAsignacionRepository.findBySocioIdWithJoins(socioId);
    }

    public List<Garage> getGaragesRentadosPorSocio(Integer socioId) {
        return garageRepository.findBySocioRentadoId(socioId);
    }

    public List<Garage> getGaragesPropiosPorSocio(Integer socioId) {
        return garageRepository.findBySocioPropietarioId(socioId);
    }

    public List<Vehiculo> getVehiculosPorSocio(Integer socioId) {
        return vehiculoRepository.findBySocioPropietarioId(socioId);
    }

    public boolean vehiculoEstaLibre(Integer vehiculoId, Integer garageId) {
    List<Garage> garagesOcupados = garageRepository.findByVehiculoAsignado(vehiculoId);

    for (Garage g : garagesOcupados) {
        if (!g.getId().equals(garageId)) {
            return false; 
        }
    }
    return true; 
}

@Transactional
public boolean adminAsignaVehiculoGarageRentado(Integer socioId, Integer garageId, Integer vehiculoId) {

    Garage garage = garageRepository.findById(garageId).orElse(null);
    Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId).orElse(null);
    if (garage == null || vehiculo == null) return false;

    if (!vehiculoEstaLibre(vehiculoId, garageId)) {
        return false;
    }

    if (garage.getSocioRentado() == null || !garage.getSocioRentado().getId().equals(socioId)) {
        return false;
    }

    garage.setVehiculoAsignado(vehiculo);
    garageRepository.save(garage);
    return true;
}




@Transactional
public boolean adminAsignaVehiculoGaragePropio(Integer socioId, Integer garageId, Integer vehiculoId) {

    Garage garage = garageRepository.findById(garageId).orElse(null);
    Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId).orElse(null);
    if (garage == null || vehiculo == null) return false;

    if (!vehiculoEstaLibre(vehiculoId, garageId)) {
        return false;
    }

    if (garage.getSocioPropietario() == null || !garage.getSocioPropietario().getId().equals(socioId)) {
        return false;
    }

    garage.setVehiculoAsignado(vehiculo);
    garageRepository.save(garage);
    return true;
}


    @Transactional
    public boolean socioAsignaVehiculoGaragePropio(Integer socioId, Integer garageId, Integer vehiculoId) {
    Garage garage = garageRepository.findById(garageId).orElse(null);
    Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId).orElse(null);
    if (garage == null || vehiculo == null) return false;

    if (!vehiculoEstaLibre(vehiculoId, garageId)) {
        return false;
    }

    if (garage.getSocioPropietario() == null || !garage.getSocioPropietario().getId().equals(socioId)) {
        return false;
    }

    garage.setVehiculoAsignado(vehiculo);
    garageRepository.save(garage);
    return true;
}
    
    @Transactional
    public boolean vaciarGaragePropio(Integer socioId, Integer garageId) {
        Garage garage = garageRepository.findById(garageId).orElse(null);

        if (garage == null) return false;

        if (garage.getSocioPropietario() == null ||
            !garage.getSocioPropietario().getId().equals(socioId)) {
            return false;
        }

        garage.setVehiculoAsignado(null);
        garageRepository.save(garage);

        return true;
    }

    @Transactional
    public boolean vaciarGarageRentado(Integer socioId, Integer garageId) {
        Garage garage = garageRepository.findById(garageId).orElse(null);

        if (garage == null) return false;

        if (garage.getSocioRentado() == null ||
            !garage.getSocioRentado().getId().equals(socioId)) {
            return false;
        }

        garage.setVehiculoAsignado(null);
        garageRepository.save(garage);

        return true;
    }

}
