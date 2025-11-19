package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.repositorios.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GarageService {

    @Autowired
    private GarageRepository garageRepository;

    public List<Garage> listarTodos() {
        return garageRepository.findAll();
    }

    public void guardar(Garage garage) {
        garageRepository.save(garage);
    }

    public void eliminar(Integer id) {
        garageRepository.deleteById(id);
    }

    public Garage buscarPorId(Integer id) {
        Optional<Garage> garage = garageRepository.findById(id);
        return garage.orElse(null);
    }
}