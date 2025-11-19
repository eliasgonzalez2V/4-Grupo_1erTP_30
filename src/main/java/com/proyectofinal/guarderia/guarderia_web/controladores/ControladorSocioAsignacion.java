package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.SocioAsignacion;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioAsignacionService;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioService;
import com.proyectofinal.guarderia.guarderia_web.servicios.GarageService;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/socioasignacion")
public class ControladorSocioAsignacion {

    @Autowired
    private SocioAsignacionService socioAsignacionService;

    @Autowired
    private SocioService socioService;

    @Autowired
    private GarageService garageService;

    @Autowired
    private VehiculoService vehiculoService;


    // -----------------------------------------------------
    //     MÉTODO CENTRAL DE SEGURIDAD
    // -----------------------------------------------------
    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }
    // ---------------------------------------------------
    // VERIFICAR SESIÓN (ADMIN O EMPLEADO)
    // ---------------------------------------------------
    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniEmpleado") == null 
            && session.getAttribute("dniAdministrador") == null;
    }

    // -----------------------------------------------------
    // LISTAR
    // -----------------------------------------------------
    @GetMapping("/listar")
    public String listarAsignaciones(Model model,
                                     HttpSession session,
                                     RedirectAttributes redirect) {

        if (noHaySesion(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("asignaciones", socioAsignacionService.listarTodos());

        return "listar-socioasignacion";
    }


    // -----------------------------------------------------
    // NUEVA ASIGNACIÓN
    // -----------------------------------------------------
    @GetMapping("/nuevo")
    public String nuevaAsignacion(Model model,
                                  HttpSession session,
                                  RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("asignacion", new SocioAsignacion());
        model.addAttribute("socios", socioService.listarTodos());
        model.addAttribute("garages", garageService.listarTodos());
        model.addAttribute("vehiculos", vehiculoService.listarTodos());

        return "form-socioasignacion";
    }


    // -----------------------------------------------------
    // GUARDAR + VALIDACIONES
    // -----------------------------------------------------
    @PostMapping("/guardar")
    public String guardarAsignacion(@ModelAttribute SocioAsignacion asignacion,
                                    HttpSession session,
                                    RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        // VALIDACIONES
        if (asignacion.getSocio() == null) {
            redirect.addFlashAttribute("error", "Debe seleccionar un socio.");
            return "redirect:/socioasignacion/nuevo";
        }

        if (asignacion.getGarage() == null) {
            redirect.addFlashAttribute("error", "Debe seleccionar un garage.");
            return "redirect:/socioasignacion/nuevo";
        }

        if (asignacion.getVehiculo() == null) {
            redirect.addFlashAttribute("error", "Debe seleccionar un vehículo.");
            return "redirect:/socioasignacion/nuevo";
        }

        socioAsignacionService.guardar(asignacion);

        redirect.addFlashAttribute("exito", "Asignación guardada correctamente.");

        return "redirect:/socioasignacion/listar";
    }


    // -----------------------------------------------------
    // EDITAR
    // -----------------------------------------------------
    @GetMapping("/editar/{id}")
    public String editarAsignacion(@PathVariable Integer id,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirect) {

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

        return "form-socioasignacion";
    }


    // -----------------------------------------------------
    // ELIMINAR
    // -----------------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminarAsignacion(@PathVariable Integer id,
                                     HttpSession session,
                                     RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        socioAsignacionService.eliminar(id);

        redirect.addFlashAttribute("exito", "Asignación eliminada correctamente.");

        return "redirect:/socioasignacion/listar";
    }
        @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";}

}
