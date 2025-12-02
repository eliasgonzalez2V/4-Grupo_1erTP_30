package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.modelos.Especialidad;
import com.proyectofinal.guarderia.guarderia_web.servicios.EspecialidadService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/especialidad")
public class ControladorEspecialidad {

    private static final Logger logger = LoggerFactory.getLogger(ControladorEspecialidad.class);

    @Autowired
    private EspecialidadService especialidadService;

    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/listar")
    public String listarEspecialidades(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("especialidades", especialidadService.listarTodos());
            return "especialidad/listar-especialidad";

        } catch (Exception e) {
            logger.error("Error en listar especialidades", e);
            redirect.addFlashAttribute("error", "Error al cargar especialidades.");
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/nuevo")
    public String nuevaEspecialidad(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("especialidad", new Especialidad());
            return "especialidad/form-especialidad";

        } catch (Exception e) {
            logger.error("Error en nueva especialidad", e);
            redirect.addFlashAttribute("error", "Error al cargar formulario.");
            return "redirect:/especialidad/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarEspecialidad(@RequestParam(required = false) Integer id,
            @RequestParam String tipoEspecialidad,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
                return "redirect:/";
            }

            if (tipoEspecialidad == null || tipoEspecialidad.trim().isEmpty()) {
                throw new CampoObligatorioException("Tipo de especialidad");
            }

            Especialidad especialidad;

            if (id != null) {
                especialidad = especialidadService.buscarPorId(id);
                if (especialidad == null) {
                    redirect.addFlashAttribute("error", "La especialidad no existe.");
                    return "redirect:/especialidad/listar";
                }
                especialidad.setTipoEspecialidad(tipoEspecialidad);
            } else {
                especialidad = new Especialidad();
                especialidad.setTipoEspecialidad(tipoEspecialidad);
            }

            especialidadService.guardar(especialidad);

            if (id == null) {
                redirect.addFlashAttribute("exito", "Especialidad registrada correctamente.");
            } else {
                redirect.addFlashAttribute("exito", "Especialidad actualizada correctamente.");
            }

            return "redirect:/especialidad/listar";

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en guardar especialidad: {}", e.getMessage());
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/especialidad/nuevo";
        } catch (Exception e) {
            logger.error("Error al guardar especialidad - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/especialidad/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarEspecialidad(@PathVariable Integer id,
            Model model,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
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
            return "especialidad/form-especialidad";

        } catch (Exception e) {
            logger.error("Error al editar especialidad - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al cargar especialidad.");
            return "redirect:/especialidad/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEspecialidad(@PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión primero.");
                return "redirect:/";
            }

            especialidadService.eliminar(id);
            redirect.addFlashAttribute("exito", "Especialidad eliminada correctamente.");
            return "redirect:/especialidad/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar especialidad - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/especialidad/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
