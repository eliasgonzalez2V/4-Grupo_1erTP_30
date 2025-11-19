package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.EmpleadoAsignacion;
import com.proyectofinal.guarderia.guarderia_web.servicios.EmpleadoAsignacionService;
import com.proyectofinal.guarderia.guarderia_web.servicios.EmpleadoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.MantenimientoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empleadoasignacion")
public class ControladorEmpleadoAsignacion {

    @Autowired
    private EmpleadoAsignacionService empleadoAsignacionService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private MantenimientoService mantenimientoService;


    // ---------------------------------------------------
    // VERIFICAR SESIÓN ADMINISTRADOR
    // ---------------------------------------------------

    private boolean noHaySesionAdmin(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }
    // ---------------------------------------------------
    // VERIFICAR SESIÓN (ADMIN O EMPLEADO)
    // ---------------------------------------------------
    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniEmpleado") == null 
            && session.getAttribute("dniAdministrador") == null;
    }

    // ---------------------------------------------------
    // LISTAR (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/listar")
    public String listarOrdenes(HttpSession session, Model model) {

        if (noHaySesion(session)) return "redirect:/";

        model.addAttribute("ordenes", empleadoAsignacionService.listarTodos());
        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));

        return "listar-empleadoasignacion";
    }


    // ---------------------------------------------------
    // NUEVA ORDEN (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/nuevo")
    public String nuevaOrden(HttpSession session, Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        model.addAttribute("orden", new EmpleadoAsignacion());
        model.addAttribute("empleados", empleadoService.listarTodos());
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("mantenimientos", mantenimientoService.listarTodos());

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));

        return "form-empleadoasignacion";
    }


    // ---------------------------------------------------
    // GUARDAR (PROTEGIDO + VALIDACIONES)
    // ---------------------------------------------------

    @PostMapping("/guardar")
    public String guardarOrden(@Valid @ModelAttribute("orden") EmpleadoAsignacion orden,
                               BindingResult result,
                               HttpSession session,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        if (result.hasErrors()) {

            // volver a cargar combos
            model.addAttribute("empleados", empleadoService.listarTodos());
            model.addAttribute("vehiculos", vehiculoService.listarTodos());
            model.addAttribute("mantenimientos", mantenimientoService.listarTodos());
            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));

            return "form-empleadoasignacion";
        }

        try {
            empleadoAsignacionService.guardar(orden);
            redirectAttributes.addFlashAttribute("success", "Asignación guardada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }

        return "redirect:/empleadoasignacion/listar";
    }


    // ---------------------------------------------------
    // EDITAR (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/editar/{id}")
    public String editarOrden(@PathVariable Integer id,
                              HttpSession session,
                              Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        EmpleadoAsignacion orden = empleadoAsignacionService.buscarPorId(id);

        model.addAttribute("orden", orden);
        model.addAttribute("empleados", empleadoService.listarTodos());
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("mantenimientos", mantenimientoService.listarTodos());

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));

        return "form-empleadoasignacion";
    }


    // ---------------------------------------------------
    // ELIMINAR (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Integer id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        try {
            empleadoAsignacionService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Asignación eliminada correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }

        return "redirect:/empleadoasignacion/listar";
    }




    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
}
