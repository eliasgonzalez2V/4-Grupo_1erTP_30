package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Administrador;
import com.proyectofinal.guarderia.guarderia_web.repositorios.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public Administrador login(Integer dni, String contrasenia) {
        return administradorRepository.findByDniAndContrasenia(dni, contrasenia);
    }

    public Administrador guardar(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    public Administrador buscarPorId(Integer id) {
        return administradorRepository.findById(id).orElse(null);
    }

    public void eliminar(Integer id) {
        administradorRepository.deleteById(id);
    }

    public Iterable<Administrador> listarTodos() {
        return administradorRepository.findAll();
    }
}