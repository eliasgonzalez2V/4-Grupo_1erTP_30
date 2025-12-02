package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Empleado;
import com.proyectofinal.guarderia.guarderia_web.repositorios.EmpleadoRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public Empleado login(Integer dni, String contrasenia) {
        try {
            return empleadoRepository.findByDniAndContrasenia(dni, contrasenia);
        } catch (Exception e) {
            throw new ValidacionException("Error en el login: " + e.getMessage());
        }
    }

    public Empleado guardar(Empleado empleado) {
        try {
            validarDNI(empleado.getDni());

            if (empleado.getId() != null) {
                Empleado empleadoExistente = empleadoRepository.findById(empleado.getId()).orElse(null);
                
                if (empleadoExistente != null) {
                    if (empleado.getContrasenia() == null || empleado.getContrasenia().trim().isEmpty()) {
                        empleado.setContrasenia(empleadoExistente.getContrasenia());
                    }
                    
                    if (!empleadoExistente.getDni().equals(empleado.getDni())) {
                        Empleado existentePorDNI = empleadoRepository.findByDni(empleado.getDni());
                        if (existentePorDNI != null) {
                            throw new ValidacionException("Ya existe otro empleado con el DNI: " + empleado.getDni());
                        }
                    }
                }
            } else {
                Empleado existente = empleadoRepository.findByDni(empleado.getDni());
                if (existente != null) {
                    throw new ValidacionException("Ya existe un empleado con el DNI: " + empleado.getDni());
                }
                
                if (empleado.getContrasenia() == null || empleado.getContrasenia().trim().isEmpty()) {
                    throw new ValidacionException("La contraseña es obligatoria para nuevos empleados");
                }
            }

            return empleadoRepository.save(empleado);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar empleado: " + e.getMessage());
        }
    }

    public Empleado buscarPorId(Integer id) {
        try {
            return empleadoRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar empleado: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) {
        try {
            empleadoRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar empleado: " + e.getMessage());
        }
    }

    public Iterable<Empleado> listarTodos() {
        try {
            return empleadoRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar empleados: " + e.getMessage());
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