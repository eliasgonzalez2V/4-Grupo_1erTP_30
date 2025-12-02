package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Administrador;
import com.proyectofinal.guarderia.guarderia_web.servicios.AdministradorService;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/administrador")
public class ControladorAdministrador {

    private static final Logger logger = LoggerFactory.getLogger(ControladorAdministrador.class);

    @Autowired
    private AdministradorService administradorService;

    @GetMapping("/")
    public String paginaPrincipal() {
        return "index";
    }

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

        try {
            if (result.hasErrors()) {
                return "redirect:/loginForm/login";
            }

            if (admin.getDni() == null) {
                throw new CampoObligatorioException("DNI");
            }

            if (admin.getContrasenia() == null || admin.getContrasenia().trim().isEmpty()) {
                throw new CampoObligatorioException("Contraseña");
            }

            Administrador adminLogueado = administradorService.login(admin.getDni(), admin.getContrasenia());

            if (adminLogueado != null) {
                session.setAttribute("dniAdministrador", adminLogueado.getDni());
                session.setAttribute("nombreAdministrador", adminLogueado.getNombre());
                session.setAttribute("idAdministrador", adminLogueado.getId());
                return "redirect:/administrador/menu";
            } else {
                redirectAttributes.addFlashAttribute("error", "DNI o Contraseña incorrectos.");
                return "redirect:/loginForm/login";
            }

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en login: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/loginForm/login";
        } catch (Exception e) {
            logger.error("Error en login administrador - DNI: {}", admin.getDni(), e);
            redirectAttributes.addFlashAttribute("error", "Error en el sistema. Intente nuevamente.");
            return "redirect:/loginForm/login";
        }
    }

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/menu")
    public String menuAdmin(HttpSession session, Model model) {
        try {
            if (noHaySesion(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("dniAdministrador", session.getAttribute("dniAdministrador"));
            return "administrador/menu-administrador";

        } catch (Exception e) {
            logger.error("Error en menu administrador", e);
            return "redirect:/loginForm/login";
        }
    }

    @GetMapping("/mis-datos")
    public String verMisDatos(HttpSession session, Model model) {
        try {
            if (noHaySesion(session)) {
                return "redirect:/loginForm/login";
            }

            Integer idAdmin = (Integer) session.getAttribute("idAdministrador");
            Administrador administrador = administradorService.buscarPorId(idAdmin);

            if (administrador != null) {
                model.addAttribute("administrador", administrador);
                return "administrador/ver-datos-administrador";
            }

            return "redirect:/administrador/menu";

        } catch (Exception e) {
            logger.error("Error en mis datos administrador", e);
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/listar")
    public String listarAdministradores(HttpSession session, Model model) {
        try {
            if (noHaySesion(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("administradores", administradorService.listarTodos());
            return "administrador/listar-administrador";

        } catch (Exception e) {
            logger.error("Error en listar administradores", e);
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/nuevo")
    public String nuevoAdministrador(HttpSession session, Model model) {
        try {
            if (noHaySesion(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("administrador", new Administrador());
            return "administrador/form-administrador";

        } catch (Exception e) {
            logger.error("Error en nuevo administrador", e);
            return "redirect:/administrador/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarAdministrador(@Valid @ModelAttribute("administrador") Administrador administrador,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {

        try {
            if (noHaySesion(session)) {
                return "redirect:/loginForm/login";
            }

            if (result.hasErrors()) {
                model.addAttribute("error", "Revisá los datos ingresados.");
                return "administrador/form-administrador";
            }

            if (administrador.getId() == null
                    && (administrador.getContrasenia() == null || administrador.getContrasenia().trim().isEmpty())) {
                model.addAttribute("error", "La contraseña es obligatoria para nuevos administradores");
                return "administrador/form-administrador";
            }

            administradorService.guardar(administrador);
            redirectAttributes.addFlashAttribute("success", "Administrador guardado correctamente");

            Integer idAdminLogueado = (Integer) session.getAttribute("idAdministrador");
            if (idAdminLogueado != null && idAdminLogueado.equals(administrador.getId())) {
                session.setAttribute("nombreAdministrador", administrador.getNombre());
                return "redirect:/administrador/menu";
            }

            return "redirect:/administrador/listar";

        } catch (Exception e) {
            logger.error("Error al guardar administrador - ID: {}", administrador.getId(), e);
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("administrador", administrador);
            return "administrador/form-administrador";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarAdministrador(@PathVariable Integer id,
            HttpSession session,
            Model model) {

        try {
            if (noHaySesion(session)) {
                return "redirect:/loginForm/login";
            }

            Administrador administrador = administradorService.buscarPorId(id);
            if (administrador != null) {
                model.addAttribute("administrador", administrador);
                return "administrador/form-administrador";
            }

            return "redirect:/administrador/listar";

        } catch (Exception e) {
            logger.error("Error al editar administrador - ID: {}", id, e);
            return "redirect:/administrador/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAdministrador(@PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            if (noHaySesion(session)) {
                return "redirect:/loginForm/login";
            }

            administradorService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Administrador eliminado correctamente");
            return "redirect:/administrador/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar administrador - ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/administrador/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private void validarCamposObligatorios(Administrador administrador) {
        if (administrador.getDni() == null) {
            throw new CampoObligatorioException("DNI");
        }
        if (administrador.getNombre() == null || administrador.getNombre().trim().isEmpty()) {
            throw new CampoObligatorioException("Nombre");
        }
        if (administrador.getDireccion() == null || administrador.getDireccion().trim().isEmpty()) {
            throw new CampoObligatorioException("Dirección");
        }
        if (administrador.getTelefono() == null || administrador.getTelefono().trim().isEmpty()) {
            throw new CampoObligatorioException("Teléfono");
        }
        if (administrador.getId() == null && (administrador.getContrasenia() == null || administrador.getContrasenia().trim().isEmpty())) {
            throw new CampoObligatorioException("Contraseña");
        }
    }
}
