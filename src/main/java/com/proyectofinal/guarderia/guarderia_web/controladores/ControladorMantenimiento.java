package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Mantenimiento;
import com.proyectofinal.guarderia.guarderia_web.servicios.MantenimientoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mantenimiento")
public class ControladorMantenimiento {

    @Autowired
    private MantenimientoService mantenimientoService;


    // ----------------------------------------------
    //     MÉTODO CENTRAL DE SEGURIDAD
    // ----------------------------------------------
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

    // ----------------------------------------------
    // LISTAR
    // ----------------------------------------------
    @GetMapping("/listar")
    public String listarMantenimientos(Model model,
                                      HttpSession session,
                                      RedirectAttributes redirect) {

        if (noHaySesion(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("mantenimientos", mantenimientoService.listarTodos());

        return "listar-mantenimiento";
    }


    // ----------------------------------------------
    // NUEVO
    // ----------------------------------------------
    @GetMapping("/nuevo")
    public String nuevoMantenimiento(Model model,
                                    HttpSession session,
                                    RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("mantenimiento", new Mantenimiento());

        return "form-mantenimiento";
    }


    // ----------------------------------------------
    // GUARDAR + VALIDACIONES
    // ----------------------------------------------
    @PostMapping("/guardar")
    public String guardarMantenimiento(@ModelAttribute Mantenimiento mantenimiento,
                                       HttpSession session,
                                       RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
            return "redirect:/";
        }



        try {
            mantenimientoService.guardar(mantenimiento);
            redirect.addFlashAttribute("exito", "Mantenimiento registrado correctamente.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }

        return "redirect:/mantenimiento/listar";
    }


    // ----------------------------------------------
    // EDITAR
    // ----------------------------------------------
    @GetMapping("/editar/{id}")
    public String editarMantenimiento(@PathVariable Integer id,
                                     Model model,
                                     HttpSession session,
                                     RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        Mantenimiento mantenimiento = mantenimientoService.buscarPorId(id);

        if (mantenimiento == null) {
            redirect.addFlashAttribute("error", "El mantenimiento no existe.");
            return "redirect:/mantenimiento/listar";
        }

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("mantenimiento", mantenimiento);

        return "form-mantenimiento";
    }


    // ----------------------------------------------
    // ELIMINAR
    // ----------------------------------------------
    @GetMapping("/eliminar/{id}")
    public String eliminarMantenimiento(@PathVariable Integer id,
                                        HttpSession session,
                                        RedirectAttributes redirect) {

        if (adminNoLogueado(session)) {
            redirect.addFlashAttribute("error", "Debe iniciar sesión.");
            return "redirect:/";
        }

        try {
            mantenimientoService.eliminar(id);
            redirect.addFlashAttribute("exito", "Mantenimiento eliminado correctamente.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }

        return "redirect:/mantenimiento/listar";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";}
}
