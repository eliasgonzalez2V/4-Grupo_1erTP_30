package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import com.proyectofinal.guarderia.guarderia_web.repositorios.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocioService {

    @Autowired
    private SocioRepository socioRepository;

    public Socio login(Integer dni, String contrasenia) {
        return socioRepository.findByDniAndContrasenia(dni, contrasenia);
    }

    public Socio guardar(Socio socio) {
        return socioRepository.save(socio);
    }

    public Socio buscarPorId(Integer id) {
        return socioRepository.findById(id).orElse(null);
    }

    public void eliminar(Integer id) {
        socioRepository.deleteById(id);
    }

    public Iterable<Socio> listarTodos() {
        return socioRepository.findAll();
    }
}