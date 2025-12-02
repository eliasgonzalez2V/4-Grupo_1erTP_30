package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.EmpleadoAsignacion;
import com.proyectofinal.guarderia.guarderia_web.repositorios.EmpleadoAsignacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoAsignacionService {

    @Autowired
    private EmpleadoAsignacionRepository empleadoAsignacionRepository;

    public List<EmpleadoAsignacion> listarTodos() {
        return empleadoAsignacionRepository.findAll();
    }

    public List<EmpleadoAsignacion> buscarPorEmpleadoId(Integer empleadoId) {
        return empleadoAsignacionRepository.findByEmpleadoId(empleadoId);
    }

    public void guardar(EmpleadoAsignacion empleadoAsignacion) {
        empleadoAsignacionRepository.save(empleadoAsignacion);
    }

    public void eliminar(Integer id) {
        empleadoAsignacionRepository.deleteById(id);
    }

    public EmpleadoAsignacion buscarPorId(Integer id) {
        Optional<EmpleadoAsignacion> asignacion = empleadoAsignacionRepository.findById(id);
        return asignacion.orElse(null);
    }
}
