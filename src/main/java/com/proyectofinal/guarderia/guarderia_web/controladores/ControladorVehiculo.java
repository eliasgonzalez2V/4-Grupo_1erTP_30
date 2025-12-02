package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import com.proyectofinal.guarderia.guarderia_web.modelos.Mantenimiento;
import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.MantenimientoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioService;
import com.proyectofinal.guarderia.guarderia_web.repositorios.GarageRepository;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vehiculo")
public class ControladorVehiculo {

    private static final Logger logger = LoggerFactory.getLogger(ControladorVehiculo.class);

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private MantenimientoService mantenimientoService;

    @Autowired
    private SocioService socioService;

    @Autowired
    private GarageRepository garageRepository;

    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    private boolean noHaySesionAdYSo(HttpSession session) {
        return session.getAttribute("dniSocio") == null
                && session.getAttribute("dniAdministrador") == null;
    }

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniSocio") == null
                && session.getAttribute("dniEmpleado") == null
                && session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/mis-vehiculos")
    public String verMisVehiculos(HttpSession session, Model model, RedirectAttributes redirect) {
        try {
            if (session.getAttribute("dniSocio") == null) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como socio.");
                return "redirect:/loginForm/login";
            }

            Integer socioId = (Integer) session.getAttribute("idSocio");
            List<Vehiculo> misVehiculos = vehiculoService.buscarVehiculosPorSocioId(socioId);

            for (Vehiculo vehiculo : misVehiculos) {
                List<Garage> garagesAsignados = garageRepository.findByVehiculoAsignadoId(vehiculo.getId());
                if (!garagesAsignados.isEmpty()) {
                    vehiculo.setGarageAsignado(garagesAsignados.get(0));
                }
            }

            model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            model.addAttribute("vehiculos", misVehiculos);
            model.addAttribute("esSocio", true);

            return "vehiculo/listar-vehiculo";

        } catch (Exception e) {
            logger.error("Error en mis-vehiculos - Socio ID: {}", session.getAttribute("idSocio"), e);
            redirect.addFlashAttribute("error", "Error al cargar vehículos.");
            return "redirect:/socio/menu";
        }
    }

    @GetMapping("/listar")
    public String listarVehiculos(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (noHaySesion(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            boolean esAdmin = session.getAttribute("dniAdministrador") != null;
            boolean esSocio = session.getAttribute("dniSocio") != null;
            boolean esEmpleado = session.getAttribute("dniEmpleado") != null;

            model.addAttribute("esAdmin", esAdmin);
            model.addAttribute("esSocio", esSocio);

            List<Vehiculo> vehiculos;

            if (esAdmin) {
                vehiculos = vehiculoService.listarTodos();
            } else if (esEmpleado) {
                Integer empleadoId = (Integer) session.getAttribute("idEmpleado");
                vehiculos = vehiculoService.buscarVehiculosPorEmpleadoId(empleadoId);
            } else {
                vehiculos = vehiculoService.listarTodos();
            }

            if (esSocio) {
                for (Vehiculo vehiculo : vehiculos) {
                    List<Garage> garagesAsignados = garageRepository.findByVehiculoAsignadoId(vehiculo.getId());
                    if (!garagesAsignados.isEmpty()) {
                        vehiculo.setGarageAsignado(garagesAsignados.get(0));
                    }
                }
            }

            model.addAttribute("vehiculos", vehiculos);

            if (esAdmin) {
                model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            } else if (esSocio) {
                model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            }

            return "vehiculo/listar-vehiculo";

        } catch (Exception e) {
            logger.error("Error en listar vehículos", e);
            redirect.addFlashAttribute("error", "Error al cargar vehículos.");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String nuevoVehiculo(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("vehiculo", new Vehiculo());
            model.addAttribute("mantenimientos", mantenimientoService.listarTodos());
            model.addAttribute("socios", socioService.listarTodos());

            return "vehiculo/form-vehiculo";

        } catch (Exception e) {
            logger.error("Error en nuevo vehículo", e);
            redirect.addFlashAttribute("error", "Error al cargar formulario.");
            return "redirect:/vehiculo/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo,
            @RequestParam(value = "mantenimiento.id", required = false) Integer mantenimientoId,
            @RequestParam(value = "socioPropietario.id", required = false) Integer socioPropietarioId,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            validarCamposObligatoriosVehiculo(vehiculo, socioPropietarioId);

            if (vehiculo.getId() != null) {
                Vehiculo vehiculoExistente = vehiculoService.buscarPorId(vehiculo.getId());
                if (vehiculoExistente != null) {
                    vehiculo.setAsignacionesEmpleado(vehiculoExistente.getAsignacionesEmpleado());
                    vehiculo.setAsignacionesSocio(vehiculoExistente.getAsignacionesSocio());
                    vehiculo.setMantenimientosContratados(vehiculoExistente.getMantenimientosContratados());
                }
            }

            if (mantenimientoId != null) {
                Mantenimiento mantenimiento = mantenimientoService.buscarPorId(mantenimientoId);
                vehiculo.setMantenimiento(mantenimiento);
            } else {
                vehiculo.setMantenimiento(null);
            }

            if (socioPropietarioId != null) {
                Socio socioPropietario = socioService.buscarPorId(socioPropietarioId);
                vehiculo.setSocioPropietario(socioPropietario);
            } else {
                vehiculo.setSocioPropietario(null);
            }

            vehiculoService.guardar(vehiculo);
            redirect.addFlashAttribute("exito", "Vehículo guardado correctamente.");

            return "redirect:/vehiculo/listar";

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en guardar vehículo: {}", e.getMessage());
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/vehiculo/nuevo";
        } catch (Exception e) {
            logger.error("Error al guardar vehículo - ID: {}", vehiculo.getId(), e);
            redirect.addFlashAttribute("error", "Error al guardar vehículo. Verifique los datos.");

            if (vehiculo.getId() == null) {
                return "redirect:/vehiculo/nuevo";
            } else {
                return "redirect:/vehiculo/editar/" + vehiculo.getId();
            }
        }
    }

    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable Integer id,
            Model model,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            Vehiculo vehiculo = vehiculoService.buscarPorId(id);

            if (vehiculo == null) {
                redirect.addFlashAttribute("error", "El vehículo no existe.");
                return "redirect:/vehiculo/listar";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("vehiculo", vehiculo);
            model.addAttribute("mantenimientos", mantenimientoService.listarTodos());
            model.addAttribute("socios", socioService.listarTodos());

            return "vehiculo/form-vehiculo";

        } catch (Exception e) {
            logger.error("Error al editar vehículo - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al cargar vehículo.");
            return "redirect:/vehiculo/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (noHaySesionAdYSo(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            vehiculoService.eliminar(id);
            redirect.addFlashAttribute("exito", "Vehículo eliminado correctamente.");

            return "redirect:/vehiculo/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar vehículo - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al eliminar vehículo.");
            return "redirect:/vehiculo/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private void validarCamposObligatoriosVehiculo(Vehiculo vehiculo, Integer socioPropietarioId) {
        if (vehiculo.getMatricula() == null || vehiculo.getMatricula().trim().isEmpty()) {
            throw new CampoObligatorioException("Matrícula");
        }
        if (vehiculo.getMarca() == null || vehiculo.getMarca().trim().isEmpty()) {
            throw new CampoObligatorioException("Marca");
        }
        if (vehiculo.getTipo() == null || vehiculo.getTipo().trim().isEmpty()) {
            throw new CampoObligatorioException("Tipo");
        }
        if (socioPropietarioId == null) {
            throw new CampoObligatorioException("Socio propietario");
        }
    }
}
