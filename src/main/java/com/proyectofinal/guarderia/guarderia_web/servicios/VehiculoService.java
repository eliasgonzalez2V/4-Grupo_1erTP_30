package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import com.proyectofinal.guarderia.guarderia_web.repositorios.VehiculoRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public List<Vehiculo> listarTodos() {
        try {
            return vehiculoRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar vehículos: " + e.getMessage());
        }
    }

    public Vehiculo buscarPorId(Integer id) {
        try {
            Optional<Vehiculo> vehiculo = vehiculoRepository.findById(id);
            return vehiculo.orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar vehículo: " + e.getMessage());
        }
    }

    public Vehiculo guardar(Vehiculo vehiculo) {
        try {
            validarCamposObligatorios(vehiculo);
            return vehiculoRepository.save(vehiculo);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar vehículo: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) {
        try {
            vehiculoRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar vehículo: " + e.getMessage());
        }
    }

    public List<Vehiculo> buscarVehiculosPorEmpleadoId(Integer empleadoId) {
        try {
            return vehiculoRepository.findVehiculosByEmpleadoId(empleadoId);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar vehículos por empleado: " + e.getMessage());
        }
    }

    public List<Vehiculo> buscarVehiculosPorSocioId(Integer socioId) {
        try {
            return vehiculoRepository.findBySocioPropietarioId(socioId);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar vehículos por socio: " + e.getMessage());
        }
    }

    private void validarCamposObligatorios(Vehiculo vehiculo) {
        if (vehiculo.getMatricula() == null || vehiculo.getMatricula().trim().isEmpty()) {
            throw new CampoObligatorioException("Matrícula");
        }
        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new CampoObligatorioException("Marca");
        }
        if (vehiculo.getTipo() == null || vehiculo.getTipo().trim().isEmpty()) {
            throw new CampoObligatorioException("Tipo");
        }
        if (vehiculo.getSocioPropietario() == null) {
            throw new CampoObligatorioException("Socio propietario");
        }
    }
}
