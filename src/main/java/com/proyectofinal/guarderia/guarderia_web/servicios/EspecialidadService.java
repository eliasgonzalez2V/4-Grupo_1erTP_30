package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Especialidad;
import com.proyectofinal.guarderia.guarderia_web.repositorios.EspecialidadRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> listarTodos() {
        try {
            return especialidadRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar especialidades: " + e.getMessage());
        }
    }

    public void guardar(Especialidad especialidad) {
        try {
            validarCamposObligatorios(especialidad);
            especialidadRepository.save(especialidad);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar especialidad: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) {
        try {
            especialidadRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar especialidad: " + e.getMessage());
        }
    }

    public Especialidad buscarPorId(Integer id) {
        try {
            Optional<Especialidad> especialidad = especialidadRepository.findById(id);
            return especialidad.orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar especialidad: " + e.getMessage());
        }
    }

    private void validarCamposObligatorios(Especialidad especialidad) {
        if (especialidad.getTipoEspecialidad() == null || especialidad.getTipoEspecialidad().trim().isEmpty()) {
            throw new CampoObligatorioException("Tipo de especialidad");
        }
    }
}
