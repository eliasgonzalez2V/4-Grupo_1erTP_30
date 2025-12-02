package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.modelos.Zona;
import com.proyectofinal.guarderia.guarderia_web.servicios.ZonaService;
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
@RequestMapping("/zona")
public class ControladorZona {

    private static final Logger logger = LoggerFactory.getLogger(ControladorZona.class);

    @Autowired
    private ZonaService zonaService;

    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/listar")
    public String listarZonas(Model model,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("zonas", zonaService.listarTodos());
            return "zona/listar-zona";

        } catch (Exception e) {
            logger.error("Error en listar zonas", e);
            redirect.addFlashAttribute("error", "Error al cargar zonas.");
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/nuevo")
    public String nuevaZona(Model model,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("zona", new Zona());
            return "zona/form-zona";

        } catch (Exception e) {
            logger.error("Error en nueva zona", e);
            redirect.addFlashAttribute("error", "Error al cargar formulario.");
            return "redirect:/zona/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarZona(@Valid @ModelAttribute Zona zona,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            if (result.hasErrors()) {
                redirect.addFlashAttribute("org.springframework.validation.BindingResult.zona", result);
                redirect.addFlashAttribute("zona", zona);
                redirect.addFlashAttribute("error", "Por favor, corrija los errores en el formulario.");

                String zonaId = zona.getId();
                if (zonaId == null || zonaId.trim().isEmpty()) {
                    return "redirect:/zona/nuevo";
                } else {
                    return "redirect:/zona/editar/" + zonaId;
                }
            }

            validarCamposObligatoriosZona(zona);

            String zonaId = zona.getId();

            if (zonaId != null && !zonaId.trim().isEmpty()) {
                Zona zonaExistente = zonaService.buscarPorId(zonaId);
                if (zonaExistente != null) {
                    zona.setGarages(zonaExistente.getGarages());
                }
            }

            zonaService.guardar(zona);
            redirect.addFlashAttribute("exito", "Zona guardada correctamente.");
            return "redirect:/zona/listar";

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en guardar zona: {}", e.getMessage());
            redirect.addFlashAttribute("error", e.getMessage());
            String zonaId = zona.getId();
            if (zonaId == null || zonaId.trim().isEmpty()) {
                return "redirect:/zona/nuevo";
            } else {
                return "redirect:/zona/editar/" + zonaId;
            }
        } catch (Exception e) {
            logger.error("Error al guardar zona - ID: {}", zona.getId(), e);
            redirect.addFlashAttribute("error", "Error al guardar la zona: " + e.getMessage());
            String zonaId = zona.getId();
            if (zonaId == null || zonaId.trim().isEmpty()) {
                return "redirect:/zona/nuevo";
            } else {
                return "redirect:/zona/editar/" + zonaId;
            }
        }
    }

    private void validarCamposObligatoriosZona(Zona zona) {
        if (zona.getId() == null || zona.getId().trim().isEmpty()) {
            throw new CampoObligatorioException("ID de zona");
        }
        if (zona.getTipoVehiculo() == null || zona.getTipoVehiculo().trim().isEmpty()) {
            throw new CampoObligatorioException("Tipo de vehículo");
        }
        if (zona.getProfundidadGarages() == null) {
            throw new CampoObligatorioException("Profundidad de garages");
        }
        if (zona.getAnchoGarages() == null) {
            throw new CampoObligatorioException("Ancho de garages");
        }
    }

    @GetMapping("/editar/{id}")
    public String editarZona(@PathVariable String id,
            Model model,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
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
            return "zona/form-zona";

        } catch (Exception e) {
            logger.error("Error al editar zona - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al cargar zona.");
            return "redirect:/zona/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarZona(@PathVariable String id,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            zonaService.eliminar(id);
            redirect.addFlashAttribute("exito", "Zona eliminada correctamente.");
            return "redirect:/zona/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar zona - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al eliminar zona.");
            return "redirect:/zona/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
