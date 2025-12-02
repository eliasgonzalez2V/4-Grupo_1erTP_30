package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.EmpleadoAsignacion;
import com.proyectofinal.guarderia.guarderia_web.servicios.EmpleadoAsignacionService;
import com.proyectofinal.guarderia.guarderia_web.servicios.EmpleadoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.MantenimientoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empleadoasignacion")
public class ControladorEmpleadoAsignacion {

    private static final Logger logger = LoggerFactory.getLogger(ControladorEmpleadoAsignacion.class);

    @Autowired
    private EmpleadoAsignacionService empleadoAsignacionService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private MantenimientoService mantenimientoService;

    private boolean noHaySesionAdmin(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniEmpleado") == null
                && session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/listar")
    public String listarOrdenes(HttpSession session, Model model) {
        try {
            if (noHaySesion(session)) {
                return "redirect:/";
            }

            boolean esAdmin = session.getAttribute("dniAdministrador") != null;
            model.addAttribute("esAdmin", esAdmin);

            List<EmpleadoAsignacion> ordenes;

            if (esAdmin) {
                ordenes = empleadoAsignacionService.listarTodos();
            } else {
                Integer empleadoId = (Integer) session.getAttribute("idEmpleado");
                ordenes = empleadoAsignacionService.buscarPorEmpleadoId(empleadoId);
            }

            model.addAttribute("ordenes", ordenes);
            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            return "asignaciones/listar-empleadoasignacion";

        } catch (Exception e) {
            logger.error("Error en listar órdenes", e);
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String nuevaOrden(HttpSession session, Model model) {
        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/";
            }

            model.addAttribute("orden", new EmpleadoAsignacion());
            model.addAttribute("empleados", empleadoService.listarTodos());
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            model.addAttribute("mantenimientos", mantenimientoService.listarTodos());
            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            return "asignaciones/form-empleadoasignacion";

        } catch (Exception e) {
            logger.error("Error en nueva orden", e);
            return "redirect:/empleadoasignacion/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarOrden(@Valid @ModelAttribute("orden") EmpleadoAsignacion orden,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/";
            }

            if (result.hasErrors()) {
                model.addAttribute("empleados", empleadoService.listarTodos());
                model.addAttribute("vehiculos", vehiculoService.listarTodos());
                model.addAttribute("mantenimientos", mantenimientoService.listarTodos());
                model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
                return "asignaciones/form-empleadoasignacion";
            }

            empleadoAsignacionService.guardar(orden);
            redirectAttributes.addFlashAttribute("success", "Asignación guardada correctamente");
            return "redirect:/empleadoasignacion/listar";

        } catch (Exception e) {
            logger.error("Error al guardar orden", e);
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/empleadoasignacion/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarOrden(@PathVariable Integer id,
            HttpSession session,
            Model model) {

        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/";
            }

            EmpleadoAsignacion orden = empleadoAsignacionService.buscarPorId(id);

            model.addAttribute("orden", orden);
            model.addAttribute("empleados", empleadoService.listarTodos());
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            model.addAttribute("mantenimientos", mantenimientoService.listarTodos());
            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            return "asignaciones/form-empleadoasignacion";

        } catch (Exception e) {
            logger.error("Error al editar orden - ID: {}", id, e);
            return "redirect:/empleadoasignacion/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/";
            }

            empleadoAsignacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Asignación eliminada correctamente");
            return "redirect:/empleadoasignacion/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar orden - ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/empleadoasignacion/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}