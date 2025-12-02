package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Administrador;
import com.proyectofinal.guarderia.guarderia_web.repositorios.AdministradorRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public Administrador login(Integer dni, String contrasenia) {
        try {
            return administradorRepository.findByDniAndContrasenia(dni, contrasenia);
        } catch (Exception e) {
            throw new ValidacionException("Error en el login: " + e.getMessage());
        }
    }

    public Administrador guardar(Administrador administrador) {
        try {
            validarDNI(administrador.getDni());

            if (administrador.getId() != null) {
                Administrador adminExistente = administradorRepository.findById(administrador.getId()).orElse(null);
                
                if (adminExistente != null) {
                    if (administrador.getContrasenia() == null || administrador.getContrasenia().trim().isEmpty()) {
                        administrador.setContrasenia(adminExistente.getContrasenia());
                    }
                    
                    if (!adminExistente.getDni().equals(administrador.getDni())) {
                        Administrador existentePorDNI = administradorRepository.findByDni(administrador.getDni());
                        if (existentePorDNI != null) {
                            throw new ValidacionException("Ya existe otro administrador con el DNI: " + administrador.getDni());
                        }
                    }
                }
            } else {
                Administrador existente = administradorRepository.findByDni(administrador.getDni());
                if (existente != null) {
                    throw new ValidacionException("Ya existe un administrador con el DNI: " + administrador.getDni());
                }
                
                if (administrador.getContrasenia() == null || administrador.getContrasenia().trim().isEmpty()) {
                    throw new ValidacionException("La contraseña es obligatoria para nuevos administradores");
                }
            }

            return administradorRepository.save(administrador);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar administrador: " + e.getMessage());
        }
    }

    public Administrador buscarPorId(Integer id) {
        try {
            return administradorRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar administrador: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) {
        try {
            administradorRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar administrador: " + e.getMessage());
        }
    }

    public Iterable<Administrador> listarTodos() {
        try {
            return administradorRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar administradores: " + e.getMessage());
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