package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.SocioAsignacion;
import com.proyectofinal.guarderia.guarderia_web.repositorios.SocioAsignacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SocioAsignacionService {

    @Autowired
    private SocioAsignacionRepository socioAsignacionRepository;

    public List<SocioAsignacion> listarTodos() {
        return socioAsignacionRepository.findAll();
    }

    public void guardar(SocioAsignacion socioAsignacion) {
        socioAsignacionRepository.save(socioAsignacion);
    }

    public void eliminar(Integer id) {
        socioAsignacionRepository.deleteById(id);
    }

    public SocioAsignacion buscarPorId(Integer id) {
        Optional<SocioAsignacion> asignacion = socioAsignacionRepository.findById(id);
        return asignacion.orElse(null);
    }
}