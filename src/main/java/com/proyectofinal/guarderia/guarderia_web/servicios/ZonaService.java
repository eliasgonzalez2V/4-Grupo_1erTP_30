package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Zona;
import com.proyectofinal.guarderia.guarderia_web.repositorios.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ZonaService {

    @Autowired
    private ZonaRepository zonaRepository;

    public List<Zona> listarTodos() {
        return zonaRepository.findAll();
    }

    public void guardar(Zona zona) {
        zonaRepository.save(zona);
    }

    public void eliminar(String id) {
        zonaRepository.deleteById(id);
    }

    public Zona buscarPorId(String id) {
        Optional<Zona> zona = zonaRepository.findById(id);
        return zona.orElse(null);
    }
}