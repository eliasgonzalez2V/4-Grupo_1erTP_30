package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vehiculo")
public class ControladorVehiculo {

    @Autowired
    private VehiculoService vehiculoService;


    // ----------------------------------------------
    // MÉTODO DE SEGURIDAD ADMIN
    // ----------------------------------------------
    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }
        // ---------------------------------------------------
    // VERIFICAR SESIÓN (ADMIN O Socio)
    // ---------------------------------------------------
    private boolean noHaySesionAdYSo(HttpSession session) {
        return session.getAttribute("dniSocio") == null
            && session.getAttribute("dniAdministrador") == null;
    }
    
    // ---------------------------------------------------
    // VERIFICAR SESIÓN (ADMIN O Socio o EMPLEADO)
    // ---------------------------------------------------
    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniSocio") == null 
            && session.getAttribute("dniEmpleado") == null 
            && session.getAttribute("dniAdministrador") == null;
    }

    // ----------------------------------------------
    // LISTAR
    // ----------------------------------------------
    @GetMapping("/listar")
    public String listarVehiculos(Model model,
                                  HttpSession session,
                                  RedirectAttributes redirect) {

        if (noHaySesion(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("vehiculos", vehiculoService.listarTodos());

        return "listar-vehiculo";
    }


    // ----------------------------------------------
    // NUEVO
    // ----------------------------------------------
    @GetMapping("/nuevo")
    public String nuevoVehiculo(Model model,
                                HttpSession session,
                                RedirectAttributes redirect) {

        if (noHaySesionAdYSo(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("vehiculo", new Vehiculo());

        return "form-vehiculo";
    }


    // ----------------------------------------------
    // GUARDAR + VALIDACIÓN
    // ----------------------------------------------
    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo,
                                  HttpSession session,
                                  RedirectAttributes redirect) {

        if (noHaySesionAdYSo(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }



        vehiculoService.guardar(vehiculo);

        redirect.addFlashAttribute("exito", "Vehículo guardado correctamente.");
        return "redirect:/vehiculo/listar";
    }


    // ----------------------------------------------
    // EDITAR
    // ----------------------------------------------
    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable Integer id,
                                 Model model,
                                 HttpSession session,
                                 RedirectAttributes redirect) {

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

        return "form-vehiculo";
    }


    // ----------------------------------------------
    // ELIMINAR
    // ----------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable Integer id,
                                   HttpSession session,
                                   RedirectAttributes redirect) {

        if (noHaySesionAdYSo(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        vehiculoService.eliminar(id);

        redirect.addFlashAttribute("exito", "Vehículo eliminado correctamente.");

        return "redirect:/vehiculo/listar";
    }
            @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";}
}
