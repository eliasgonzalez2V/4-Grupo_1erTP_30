package com.proyectofinal.guarderia.guarderia_web.servicios;

import com.proyectofinal.guarderia.guarderia_web.modelos.Mantenimiento;
import com.proyectofinal.guarderia.guarderia_web.modelos.MantenimientoContratado;
import com.proyectofinal.guarderia.guarderia_web.modelos.dto.MantenimientoSocioDTO;
import com.proyectofinal.guarderia.guarderia_web.repositorios.MantenimientoContratadoRepository;
import com.proyectofinal.guarderia.guarderia_web.repositorios.MantenimientoRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.excepciones.ValidacionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MantenimientoService {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Autowired
    private MantenimientoContratadoRepository mantenimientoContratadoRepository;

    public List<Mantenimiento> listarTodos() {
        try {
            return mantenimientoRepository.findAll();
        } catch (Exception e) {
            throw new ValidacionException("Error al listar mantenimientos: " + e.getMessage());
        }
    }

    public Mantenimiento buscarPorId(Integer id) {
        try {
            Optional<Mantenimiento> mantenimiento = mantenimientoRepository.findById(id);
            return mantenimiento.orElse(null);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar mantenimiento: " + e.getMessage());
        }
    }

    public Mantenimiento guardar(Mantenimiento mantenimiento) {
        try {
            validarCamposObligatorios(mantenimiento);
            return mantenimientoRepository.save(mantenimiento);
        } catch (CampoObligatorioException | ValidacionException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacionException("Error al guardar mantenimiento: " + e.getMessage());
        }
    }

    public void eliminar(Integer id) {
        try {
            mantenimientoRepository.deleteById(id);
        } catch (Exception e) {
            throw new ValidacionException("Error al eliminar mantenimiento: " + e.getMessage());
        }
    }

    public List<Mantenimiento> buscarMantenimientosPorEmpleadoId(Integer empleadoId) {
        try {
            return mantenimientoRepository.findMantenimientosByEmpleadoId(empleadoId);
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar mantenimientos por empleado: " + e.getMessage());
        }
    }

    public List<Mantenimiento> buscarMantenimientosPorSocioId(Integer socioId) {
        try {
            List<MantenimientoContratado> mantenimientosContratados = mantenimientoContratadoRepository.findBySocioId(socioId);

            return mantenimientosContratados.stream()
                    .map(MantenimientoContratado::getMantenimiento)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar mantenimientos por socio: " + e.getMessage());
        }
    }

    public List<MantenimientoSocioDTO> buscarMantenimientosConVehiculosPorSocioId(Integer socioId) {
        try {
            List<MantenimientoContratado> mantenimientosContratados = mantenimientoContratadoRepository.findBySocioId(socioId);

            return mantenimientosContratados.stream()
                    .map(mc -> new MantenimientoSocioDTO(
                    mc.getMantenimiento().getId(),
                    mc.getMantenimiento().getTipoMantenimiento(),
                    mc.getMantenimiento().getEspecialidadMantenimiento(),
                    mc.getVehiculo().getMatricula(),
                    mc.getVehiculo().getMarca()
            ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ValidacionException("Error al buscar mantenimientos con vehículos por socio: " + e.getMessage());
        }
    }

    private void validarCamposObligatorios(Mantenimiento mantenimiento) {
        if (mantenimiento.getTipoMantenimiento() == null || mantenimiento.getTipoMantenimiento().trim().isEmpty()) {
            throw new CampoObligatorioException("Tipo de mantenimiento");
        }
        if (mantenimiento.getEspecialidadMantenimiento() == null || mantenimiento.getEspecialidadMantenimiento().trim().isEmpty()) {
            throw new CampoObligatorioException("Especialidad de mantenimiento");
        }
    }
}
