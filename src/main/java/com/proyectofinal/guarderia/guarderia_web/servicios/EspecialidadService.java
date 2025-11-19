package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Especialidad;
import com.proyectofinal.guarderia.guarderia_web.repositorios.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> listarTodos() {
        return especialidadRepository.findAll();
    }

    public void guardar(Especialidad especialidad) {
        especialidadRepository.save(especialidad);
    }

    public void eliminar(Integer id) {
        especialidadRepository.deleteById(id);
    }

    public Especialidad buscarPorId(Integer id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        return especialidad.orElse(null);
    }
}