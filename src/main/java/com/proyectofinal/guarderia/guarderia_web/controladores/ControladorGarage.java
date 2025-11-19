package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.servicios.GarageService;
import com.proyectofinal.guarderia.guarderia_web.servicios.ZonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/garage")
public class ControladorGarage {

    @Autowired
    private GarageService garageService;

    @Autowired
    private ZonaService zonaService;


    // ----------------------------------------------------
    // MÉTODO CENTRAL DE SEGURIDAD (mismo sistema que usás)
    // ----------------------------------------------------
    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }


    // ----------------------------------------------------
    // LISTAR
    // ----------------------------------------------------
    @GetMapping("/listar")
    public String listarGarages(Model model,
                                HttpSession session,
                                RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("garages", garageService.listarTodos());

        return "listar-garage";
    }


    // ----------------------------------------------------
    // NUEVO
    // ----------------------------------------------------
    @GetMapping("/nuevo")
    public String nuevoGarage(Model model,
                              HttpSession session,
                              RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("garage", new Garage());
        model.addAttribute("zonas", zonaService.listarTodos());

        return "form-garage";
    }


    // ----------------------------------------------------
    // GUARDAR + VALIDACIÓN
    // ----------------------------------------------------
    @PostMapping("/guardar")
    public String guardarGarage(@ModelAttribute Garage garage,
                                HttpSession session,
                                RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }



        garageService.guardar(garage);

        redirect.addFlashAttribute("exito", "Garage guardado correctamente.");

        return "redirect:/garage/listar";
    }


    // ----------------------------------------------------
    // EDITAR
    // ----------------------------------------------------
    @GetMapping("/editar/{id}")
    public String editarGarage(@PathVariable Integer id,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        Garage garage = garageService.buscarPorId(id);

        if (garage == null) {
            redirect.addFlashAttribute("error", "El garage no existe.");
            return "redirect:/garage/listar";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("garage", garage);
        model.addAttribute("zonas", zonaService.listarTodos());

        return "form-garage";
    }


    // ----------------------------------------------------
    // ELIMINAR
    // ----------------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminarGarage(@PathVariable Integer id,
                                 HttpSession session,
                                 RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        garageService.eliminar(id);

        redirect.addFlashAttribute("exito", "Garage eliminado correctamente.");

        return "redirect:/garage/listar";
    }
            @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";}
}
