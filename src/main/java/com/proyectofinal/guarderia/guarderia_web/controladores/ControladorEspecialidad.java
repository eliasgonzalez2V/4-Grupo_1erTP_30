package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Especialidad;
import com.proyectofinal.guarderia.guarderia_web.servicios.EspecialidadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/especialidad")
public class ControladorEspecialidad {

    @Autowired
    private EspecialidadService especialidadService;

    // ----------------------------------------------------
    // MÉTODO PARA VERIFICAR LOGIN DE ADMINISTRADOR
    // ----------------------------------------------------
    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    // ----------------------------------------------------
    // LISTAR
    // ----------------------------------------------------
    @GetMapping("/listar")
    public String listarEspecialidades(Model model, HttpSession session, RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("especialidades", especialidadService.listarTodos());
        return "listar-especialidad";
    }

    // ----------------------------------------------------
    // NUEVO
    // ----------------------------------------------------
    @GetMapping("/nuevo")
    public String nuevaEspecialidad(Model model, HttpSession session, RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("especialidad", new Especialidad());
        return "form-especialidad";
    }

    // ----------------------------------------------------
    // GUARDAR (con validación simple)
    // ----------------------------------------------------
    @PostMapping("/guardar")
    public String guardarEspecialidad(@ModelAttribute Especialidad especialidad,
                                       HttpSession session,
                                       RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
            return "redirect:/";
        }


        especialidadService.guardar(especialidad);
        redirect.addFlashAttribute("exito", "Especialidad registrada correctamente.");
        return "redirect:/especialidad/listar";
    }

    // ----------------------------------------------------
    // EDITAR
    // ----------------------------------------------------
    @GetMapping("/editar/{id}")
    public String editarEspecialidad(@PathVariable Integer id,
                                     Model model,
                                     HttpSession session,
                                     RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
            return "redirect:/";
        }

        Especialidad especialidad = especialidadService.buscarPorId(id);

        if (especialidad == null) {
            redirect.addFlashAttribute("error", "La especialidad no existe.");
            return "redirect:/especialidad/listar";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("especialidad", especialidad);

        return "form-especialidad";
    }

    // ----------------------------------------------------
    // ELIMINAR
    // ----------------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminarEspecialidad(@PathVariable Integer id,
                                       HttpSession session,
                                       RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
            return "redirect:/";
        }

        especialidadService.eliminar(id);
        redirect.addFlashAttribute("exito", "Especialidad eliminada.");

        return "redirect:/especialidad/listar";
    }
        @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
