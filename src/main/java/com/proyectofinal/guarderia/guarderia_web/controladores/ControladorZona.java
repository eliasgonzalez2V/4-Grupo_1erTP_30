package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Zona;
import com.proyectofinal.guarderia.guarderia_web.servicios.ZonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/zona")
public class ControladorZona {

    @Autowired
    private ZonaService zonaService;


    // ----------------------------------------------
    //    MÉTODO CENTRAL DE SEGURIDAD
    // ----------------------------------------------
    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }


    // ----------------------------------------------
    // LISTAR
    // ----------------------------------------------
    @GetMapping("/listar")
    public String listarZonas(Model model,
                              HttpSession session,
                              RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("zonas", zonaService.listarTodos());

        return "listar-zona";
    }


    // ----------------------------------------------
    // NUEVO
    // ----------------------------------------------
    @GetMapping("/nuevo")
    public String nuevaZona(Model model,
                            HttpSession session,
                            RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("zona", new Zona());

        return "form-zona";
    }


    // ----------------------------------------------
    // GUARDAR + VALIDACIONES
    // ----------------------------------------------
    @PostMapping("/guardar")
    public String guardarZona(@ModelAttribute Zona zona,
                              HttpSession session,
                              RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }



        zonaService.guardar(zona);

        redirect.addFlashAttribute("exito", "Zona guardada correctamente.");

        return "redirect:/zona/listar";
    }


    // ----------------------------------------------
    // EDITAR
    // ----------------------------------------------
    @GetMapping("/editar/{id}")
    public String editarZona(@PathVariable String id,
                             Model model,
                             HttpSession session,
                             RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        Zona zona = zonaService.buscarPorId(id);

        if (zona == null) {
            redirect.addFlashAttribute("error", "La zona no existe.");
            return "redirect:/zona/listar";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("zona", zona);

        return "form-zona";
    }


    // ----------------------------------------------
    // ELIMINAR
    // ----------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminarZona(@PathVariable String id,
                               HttpSession session,
                               RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        zonaService.eliminar(id);

        redirect.addFlashAttribute("exito", "Zona eliminada correctamente.");

        return "redirect:/zona/listar";
    }
                @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";}
}
