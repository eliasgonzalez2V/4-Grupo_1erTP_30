package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.SocioAsignacion;
import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioAsignacionService;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioService;
import com.proyectofinal.guarderia.guarderia_web.servicios.GarageService;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/socioasignacion")
public class ControladorSocioAsignacion {

    private static final Logger logger = LoggerFactory.getLogger(ControladorSocioAsignacion.class);

    @Autowired
    private SocioAsignacionService socioAsignacionService;

    @Autowired
    private SocioService socioService;

    @Autowired
    private GarageService garageService;

    @Autowired
    private VehiculoService vehiculoService;

    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniEmpleado") == null
                && session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/mis-rentas")
    public String verMisRentas(HttpSession session, Model model, RedirectAttributes redirect) {
        try {
            if (session.getAttribute("dniSocio") == null) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como socio.");
                return "redirect:/loginForm/login";
            }

            Integer socioId = (Integer) session.getAttribute("idSocio");
            List<SocioAsignacion> misRentas = socioAsignacionService.buscarPorSocioIdWithJoins(socioId);

            model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            model.addAttribute("asignaciones", misRentas);
            return "asignaciones/listar-socioasignacion";

        } catch (Exception e) {
            logger.error("Error en mis rentas - Socio ID: {}", session.getAttribute("idSocio"), e);
            redirect.addFlashAttribute("error", "Error al cargar rentas.");
            return "redirect:/socio/menu";
        }
    }

    @GetMapping("/listar")
    public String listarAsignaciones(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (noHaySesion(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("asignaciones", socioAsignacionService.listarTodosWithJoins());
            return "asignaciones/listar-socioasignacion";

        } catch (Exception e) {
            logger.error("Error en listar asignaciones", e);
            redirect.addFlashAttribute("error", "Error al cargar asignaciones.");
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/gestion-rentas")
    public String gestionRentasAdmin(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("socios", socioService.listarTodos());
            return "administrador/gestion-rentas-admin";

        } catch (Exception e) {
            logger.error("Error en gestión rentas", e);
            redirect.addFlashAttribute("error", "Error al cargar gestión de rentas.");
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/socio-rentas/{socioId}")
    @ResponseBody
    public String getDatosSocioRentas(@PathVariable Integer socioId, Model model) {
        try {
            List<Garage> garagesRentados = socioAsignacionService.getGaragesRentadosPorSocio(socioId);
            List<Vehiculo> vehiculosSocio = socioAsignacionService.getVehiculosPorSocio(socioId);

            StringBuilder html = new StringBuilder();

            html.append("<h5>Garages Rentados por el Socio</h5>");
            if (garagesRentados.isEmpty()) {
                html.append("<p class='text-muted'>No hay garages rentados</p>");
            } else {
                html.append("<select name='garageId' class='form-select mb-3' required>");
                html.append("<option value=''>Seleccione un garage</option>");
                for (Garage garage : garagesRentados) {
                    html.append("<option value='").append(garage.getId()).append("'>")
                            .append("Garage ").append(garage.getId())
                            .append(" - Zona ").append(garage.getZona() != null ? garage.getZona().getId() : "N/A")
                            .append("</option>");
                }
                html.append("</select>");
            }

            html.append("<h5>Vehículos del Socio</h5>");
            if (vehiculosSocio.isEmpty()) {
                html.append("<p class='text-muted'>No hay vehículos</p>");
            } else {
                html.append("<select name='vehiculoId' class='form-select mb-3' required>");
                html.append("<option value=''>Seleccione un vehículo</option>");
                for (Vehiculo vehiculo : vehiculosSocio) {
                    html.append("<option value='").append(vehiculo.getId()).append("'>")
                            .append(vehiculo.getMarca()).append(" - ").append(vehiculo.getMatricula())
                            .append("</option>");
                }
                html.append("</select>");
            }

            if (!garagesRentados.isEmpty() && !vehiculosSocio.isEmpty()) {
                html.append("<button type='submit' class='btn btn-success'>Asignar Vehículo al Garage</button>");
            }

            return html.toString();

        } catch (Exception e) {
            logger.error("Error en datos socio rentas - Socio ID: {}", socioId, e);
            return "<div class='alert alert-danger'>Error al cargar datos</div>";
        }
    }

    @PostMapping("/asignar-renta")
    public String asignarVehiculoGarageRentado(
            @RequestParam Integer socioId,
            @RequestParam Integer garageId,
            @RequestParam Integer vehiculoId,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            boolean exito = socioAsignacionService.adminAsignaVehiculoGarageRentado(socioId, garageId, vehiculoId);

            if (exito) {
                redirect.addFlashAttribute("exito", "Vehículo asignado correctamente al garage rentado.");
            } else {
                redirect.addFlashAttribute("error", "Error en la asignación.");
            }

            return "redirect:/socioasignacion/gestion-rentas";

        } catch (Exception e) {
            logger.error("Error en asignar renta - Socio: {}, Garage: {}, Vehículo: {}", 
                        socioId, garageId, vehiculoId, e);
            redirect.addFlashAttribute("error", "Error en la asignación: " + e.getMessage());
            return "redirect:/socioasignacion/gestion-rentas";
        }
    }

    @GetMapping("/nuevo")
    public String nuevaAsignacion(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("asignacion", new SocioAsignacion());
            model.addAttribute("socios", socioService.listarTodos());
            model.addAttribute("garages", garageService.listarTodos());
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            return "asignaciones/form-socioasignacion";

        } catch (Exception e) {
            logger.error("Error en nueva asignación", e);
            redirect.addFlashAttribute("error", "Error al cargar formulario.");
            return "redirect:/socioasignacion/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarAsignacion(@ModelAttribute SocioAsignacion asignacion,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            if (asignacion.getSocio() == null || asignacion.getSocio().getId() == null) {
                redirect.addFlashAttribute("error", "Debe seleccionar un socio.");
                return "redirect:/socioasignacion/nuevo";
            }

            if (asignacion.getGarage() == null || asignacion.getGarage().getId() == null) {
                redirect.addFlashAttribute("error", "Debe seleccionar un garage.");
                return "redirect:/socioasignacion/nuevo";
            }

            if (asignacion.getVehiculo() == null || asignacion.getVehiculo().getId() == null) {
                redirect.addFlashAttribute("error", "Debe seleccionar un vehículo.");
                return "redirect:/socioasignacion/nuevo";
            }

            Vehiculo vehiculo = vehiculoService.buscarPorId(asignacion.getVehiculo().getId());
            if (vehiculo == null || !vehiculo.getSocioPropietario().getId().equals(asignacion.getSocio().getId())) {
                redirect.addFlashAttribute("error", "El vehículo seleccionado no pertenece al socio.");
                return "redirect:/socioasignacion/nuevo";
            }

            socioAsignacionService.guardar(asignacion);
            redirect.addFlashAttribute("exito", "Asignación guardada correctamente.");
            return "redirect:/socioasignacion/listar";

        } catch (Exception e) {
            logger.error("Error al guardar asignación", e);
            redirect.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/socioasignacion/nuevo";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarAsignacion(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            SocioAsignacion asignacion = socioAsignacionService.buscarPorId(id);

            if (asignacion == null) {
                redirect.addFlashAttribute("error", "La asignación no existe.");
                return "redirect:/socioasignacion/listar";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("asignacion", asignacion);
            model.addAttribute("socios", socioService.listarTodos());
            model.addAttribute("garages", garageService.listarTodos());
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            return "asignaciones/form-socioasignacion";

        } catch (Exception e) {
            logger.error("Error al editar asignación - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al cargar asignación.");
            return "redirect:/socioasignacion/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAsignacion(@PathVariable Integer id, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            socioAsignacionService.eliminar(id);
            redirect.addFlashAttribute("exito", "Asignación eliminada correctamente.");
            return "redirect:/socioasignacion/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar asignación - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al eliminar asignación.");
            return "redirect:/socioasignacion/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}