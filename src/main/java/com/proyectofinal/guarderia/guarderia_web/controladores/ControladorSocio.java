package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/socio")
public class ControladorSocio {

    @Autowired
    private SocioService socioService;

    // ---------------------------------------------------
    // LOGIN
    // ---------------------------------------------------

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("socio", new Socio());
        return "login-socio";
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("socio") Socio socio,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (result.hasErrors()) {
            return "login-socio";
        }

        Socio socioLogueado = socioService.login(socio.getDni(), socio.getContrasenia());

        if (socioLogueado != null) {

            // Guardar datos en sesión
            session.setAttribute("dniSocio", socioLogueado.getDni());
            session.setAttribute("nombreSocio", socioLogueado.getNombre());
            session.setAttribute("idSocio", socioLogueado.getId());

            return "redirect:/socio/menu";
        } else {
            redirectAttributes.addFlashAttribute("error", "DNI o Contraseña incorrectos.");
            return "redirect:/socio/login";
        }
    }

    // ---------------------------------------------------
    // VERIFICAR SESIÓN (Socio)
    // ---------------------------------------------------
    private boolean noHaySesionSocio(HttpSession session) {
        return session.getAttribute("dniSocio") == null;
    }
    // ---------------------------------------------------
    // VERIFICAR SESIÓN (ADMIN )
    // ---------------------------------------------------
    private boolean noHaySesionAdmin(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    // ---------------------------------------------------
    // MENÚ DEL SOCIO (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/menu")
    public String menuSocio(HttpSession session, Model model) {

        if (noHaySesionSocio(session)) return "redirect:/socio/login";

        model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
        model.addAttribute("dniSocio", session.getAttribute("dniSocio"));

        return "menu-socio";
    }

    // ---------------------------------------------------
    // CRUD socios (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/listar")
    public String listarSocios(HttpSession session, Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        model.addAttribute("socios", socioService.listarTodos());
        return "listar-socio";
    }

    @GetMapping("/nuevo")
    public String nuevoSocio(HttpSession session, Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        model.addAttribute("socio", new Socio());
        return "form-socio";
    }

    @PostMapping("/guardar")
    public String guardarSocio(@Valid @ModelAttribute Socio socio,
                               BindingResult result,
                               HttpSession session) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        if (result.hasErrors()) {
            return "form-socio";
        }

        socioService.guardar(socio);
        return "redirect:/socio/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarSocio(@PathVariable Integer id, HttpSession session, Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        model.addAttribute("socio", socioService.buscarPorId(id));
        return "form-socio";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarSocio(@PathVariable Integer id, HttpSession session) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        socioService.eliminar(id);
        return "redirect:/socio/listar";
    }

    // ---------------------------------------------------
    // LOGOUT
    // ---------------------------------------------------

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
