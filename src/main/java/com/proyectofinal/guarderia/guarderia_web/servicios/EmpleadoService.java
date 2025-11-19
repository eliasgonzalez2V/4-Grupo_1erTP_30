package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Empleado;
import com.proyectofinal.guarderia.guarderia_web.repositorios.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public Empleado login(Integer dni, String contrasenia) {
        return empleadoRepository.findByDniAndContrasenia(dni, contrasenia);
    }

    public Empleado guardar(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public Empleado buscarPorId(Integer id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    public void eliminar(Integer id) {
        empleadoRepository.deleteById(id);
    }

    public Iterable<Empleado> listarTodos() {
        return empleadoRepository.findAll();
    }
}