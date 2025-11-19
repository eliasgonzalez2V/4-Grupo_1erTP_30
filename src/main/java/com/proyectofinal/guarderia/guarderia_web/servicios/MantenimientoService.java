package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Mantenimiento;
import com.proyectofinal.guarderia.guarderia_web.repositorios.MantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MantenimientoService {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    public List<Mantenimiento> listarTodos() {
        return mantenimientoRepository.findAll();
    }

    public void guardar(Mantenimiento mantenimiento) {
        mantenimientoRepository.save(mantenimiento);
    }

    public void eliminar(Integer id) {
        mantenimientoRepository.deleteById(id);
    }

    public Mantenimiento buscarPorId(Integer id) {
        Optional<Mantenimiento> mantenimiento = mantenimientoRepository.findById(id);
        return mantenimiento.orElse(null);
    }
}