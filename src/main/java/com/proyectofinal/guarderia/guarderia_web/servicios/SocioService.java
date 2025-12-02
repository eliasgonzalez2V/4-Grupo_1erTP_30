package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import com.proyectofinal.guarderia.guarderia_web.repositorios.SocioRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocioService {

    @Autowired
    private SocioRepository socioRepository;

    public Socio login(Integer dni, String contrasenia) {
        try {
            return socioRepository.findByDniAndContrasenia(dni, contrasenia);
        } catch (Exception e) {
            throw new ValidacionException("Error en el login: " + e.getMessage());
        }
    }

    public Socio guardar(Socio socio) {
        try {
            validarDNI(socio.getDni());

            if (socio.getId() != null) {
                Socio socioExistente = socioRepository.findById(socio.getId()).orElse(null);
                
                if (socioExistente != null) {
                    if (socio.getContrasenia() == null || socio.getContrasenia().trim().isEmpty()) {
                        socio.setContrasenia(socioExistente.getContrasenia());
                    }
                    
                    if (!socioExistente.getDni().equals(socio.getDni())) {
                        Socio existentePorDNI = socioRepository.findByDni(socio.getDni());
                        if (existentePorDNI != null) {
                            throw new ValidacionException("Ya existe otro socio con el DNI: " + socio.getDni());
                        }
                    }
                }
            } else {
                Socio existente = socioRepository.findByDni(socio.getDni());
                if (existente != null) {
                    throw new ValidacionException("Ya existe un socio con el DNI: " + socio.getDni());
                }
                
                if (socio.getContrasenia() == null || socio.getContrasenia().trim().isEmpty()) {
                    throw new ValidacionException("La contraseña es obligatoria para nuevos socios");
                }
            }

            return socioRepository.save(socio);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar socio: " + e.getMessage());
        }
    }

    public Socio buscarPorId(Integer id) {
        try {
            return socioRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar socio: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) {
        try {
            socioRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar socio: " + e.getMessage());
        }
    }

    public Iterable<Socio> listarTodos() {
        try {
            return socioRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar socios: " + e.getMessage());
        }
    }

    private void validarDNI(Integer dni) {
        if (dni == null) {
            throw new CampoObligatorioException("DNI");
        }

        String dniStr = dni.toString();

        if (dniStr.length() < 8) {
            throw new ValidacionException("El DNI debe tener al menos 8 dígitos");
        }

        if (dni < 0) {
            throw new ValidacionException("El DNI no puede ser negativo");
        }

        if (dniStr.length() > 15) {
            throw new ValidacionException("El DNI no puede tener más de 15 dígitos");
        }
    }
}