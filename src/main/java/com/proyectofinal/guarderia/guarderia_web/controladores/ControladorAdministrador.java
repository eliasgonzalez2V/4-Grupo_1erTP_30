package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Administrador;
import com.proyectofinal.guarderia.guarderia_web.servicios.AdministradorService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administrador")
public class ControladorAdministrador {

    @Autowired
    private AdministradorService administradorService;

    // ---------------------------------------------------
    // HOME
    // ---------------------------------------------------

    @GetMapping("/")
    public String paginaPrincipal() {
        return "index";
    }

    // ---------------------------------------------------
    // LOGIN
    // ---------------------------------------------------

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("administrador", new Administrador());
        return "login-administrador";
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("administrador") Administrador admin,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (result.hasErrors()) {
            return "login-administrador";
        }

        Administrador adminLogueado = administradorService.login(admin.getDni(), admin.getContrasenia());

        if (adminLogueado != null) {
            session.setAttribute("dniAdministrador", adminLogueado.getDni());
            session.setAttribute("nombreAdministrador", adminLogueado.getNombre());
            session.setAttribute("idAdministrador", adminLogueado.getId());

            return "redirect:/administrador/menu";
        } else {
            redirectAttributes.addFlashAttribute("error", "DNI o Contraseña incorrectos.");
            return "redirect:/administrador/login";
        }
    }

    // ---------------------------------------------------
    // VERIFICAR SESIÓN
    // ---------------------------------------------------

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    // ---------------------------------------------------
    // MENÚ (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/menu")
    public String menuAdmin(HttpSession session, Model model) {

        if (noHaySesion(session)) return "redirect:/administrador/login";

        model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
        model.addAttribute("dniAdministrador", session.getAttribute("dniAdministrador"));

        return "menu-administrador";
    }

    // ---------------------------------------------------
    // CRUD ADMINISTRADORES (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/listar")
    public String listarAdministradores(HttpSession session, Model model) {

        if (noHaySesion(session)) return "redirect:/";

        model.addAttribute("administradores", administradorService.listarTodos());
        return "listar-administrador";
    }

    @GetMapping("/nuevo")
    public String nuevoAdministrador(HttpSession session, Model model) {

        if (noHaySesion(session)) return "redirect:/";

        model.addAttribute("administrador", new Administrador());
        return "form-administrador";
    }

    @PostMapping("/guardar")
    public String guardarAdministrador(@Valid @ModelAttribute("administrador") Administrador administrador,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       HttpSession session) {

        if (noHaySesion(session)) return "redirect:/";

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Revisá los datos ingresados.");
            return "form-administrador";
        }

        try {
            administradorService.guardar(administrador);
            redirectAttributes.addFlashAttribute("success", "Administrador guardado correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }

        return "redirect:/administrador/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarAdministrador(@PathVariable Integer id,
                                      HttpSession session,
                                      Model model) {

        if (noHaySesion(session)) return "redirect:/";

        Administrador administrador = administradorService.buscarPorId(id);
        if (administrador != null) {
            model.addAttribute("administrador", administrador);
            return "form-administrador";
        }

        return "redirect:/administrador/listar";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAdministrador(@PathVariable Integer id,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {

        if (noHaySesion(session)) return "redirect:/";

        try {
            administradorService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Administrador eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }

        return "redirect:/administrador/listar";
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
