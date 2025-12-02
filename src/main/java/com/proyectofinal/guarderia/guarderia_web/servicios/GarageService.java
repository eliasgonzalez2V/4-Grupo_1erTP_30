package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.repositorios.GarageRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GarageService {

    @Autowired
    private GarageRepository garageRepository;

    public List<Garage> listarTodos() {
        try {
            return garageRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar garages: " + e.getMessage());
        }
    }

    public Garage guardar(Garage garage) {
        try {
            validarCamposObligatorios(garage);
            return garageRepository.save(garage);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar garage: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) {
        try {
            garageRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar garage: " + e.getMessage());
        }
    }

    public Garage buscarPorId(Integer id) {
        try {
            Optional<Garage> garage = garageRepository.findById(id);
            return garage.orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar garage: " + e.getMessage());
        }
    }

    private void validarCamposObligatorios(Garage garage) {
        if (garage.getZona() == null || garage.getZona().getId() == null) {
            throw new CampoObligatorioException("Zona");
        }
    }
}
