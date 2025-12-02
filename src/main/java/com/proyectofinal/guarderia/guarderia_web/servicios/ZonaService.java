package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Zona;
import com.proyectofinal.guarderia.guarderia_web.repositorios.ZonaRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    public List<Zona> listarTodos() {
        try {
            return zonaRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar zonas: " + e.getMessage());
        }
    }

    public void guardar(Zona zona) {
        try {
            validarCamposObligatorios(zona);
            zonaRepository.save(zona);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar zona: " + e.getMessage());
        }
    }

    public void eliminar(String id) {
        try {
            zonaRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar zona: " + e.getMessage());
        }
    }

    public Zona buscarPorId(String id) {
        try {
            Optional<Zona> zona = zonaRepository.findById(id);
            return zona.orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar zona: " + e.getMessage());
        }
    }

    private void validarCamposObligatorios(Zona zona) {
        if (zona.getId() == null || zona.getId().trim().isEmpty()) {
            throw new CampoObligatorioException("ID de zona");
        }
        if (zona.getTipoVehiculo() == null || zona.getTipoVehiculo().trim().isEmpty()) {
            throw new CampoObligatorioException("Tipo de vehículo");
        }
        if (zona.getProfundidadGarages() == null) {
            throw new CampoObligatorioException("Profundidad de garages");
        }
        if (zona.getAnchoGarages() == null) {
            throw new CampoObligatorioException("Ancho de garages");
        }
    }
}
