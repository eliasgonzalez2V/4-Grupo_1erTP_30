package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import com.proyectofinal.guarderia.guarderia_web.modelos.Mantenimiento;
import com.proyectofinal.guarderia.guarderia_web.modelos.dto.MantenimientoSocioDTO;
import com.proyectofinal.guarderia.guarderia_web.servicios.MantenimientoService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mantenimiento")
public class ControladorMantenimiento {

    private static final Logger logger = LoggerFactory.getLogger(ControladorMantenimiento.class);

    @Autowired
    private MantenimientoService mantenimientoService;

    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("dniEmpleado") == null
                && session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/mis-mantenimientos")
    public String verMisMantenimientos(HttpSession session, Model model, RedirectAttributes redirect) {
        try {
            if (session.getAttribute("dniSocio") == null) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como socio.");
                return "redirect:/loginForm/login";
            }

            Integer socioId = (Integer) session.getAttribute("idSocio");
            List<MantenimientoSocioDTO> misMantenimientos = mantenimientoService.buscarMantenimientosConVehiculosPorSocioId(socioId);

            model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            model.addAttribute("mantenimientos", misMantenimientos);
            model.addAttribute("esSocio", true);
            return "mantenimiento/listar-mantenimiento";

        } catch (Exception e) {
            logger.error("Error en mis mantenimientos - Socio ID: {}", session.getAttribute("idSocio"), e);
            redirect.addFlashAttribute("error", "Error al cargar mantenimientos.");
            return "redirect:/socio/menu";
        }
    }

    @GetMapping("/listar")
    public String listarMantenimientos(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (noHaySesion(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            boolean esAdmin = session.getAttribute("dniAdministrador") != null;
            boolean esSocio = session.getAttribute("dniSocio") != null;
            boolean esEmpleado = session.getAttribute("dniEmpleado") != null;

            model.addAttribute("esAdmin", esAdmin);
            model.addAttribute("esSocio", esSocio);

            List<Mantenimiento> mantenimientos;

            if (esAdmin) {
                mantenimientos = mantenimientoService.listarTodos();
            } else if (esEmpleado) {
                Integer empleadoId = (Integer) session.getAttribute("idEmpleado");
                mantenimientos = mantenimientoService.buscarMantenimientosPorEmpleadoId(empleadoId);
            } else {
                mantenimientos = mantenimientoService.listarTodos();
            }

            model.addAttribute("mantenimientos", mantenimientos);

            if (esAdmin) {
                model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            } else if (esSocio) {
                model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            }

            return "mantenimiento/listar-mantenimiento";

        } catch (Exception e) {
            logger.error("Error en listar mantenimientos", e);
            redirect.addFlashAttribute("error", "Error al cargar mantenimientos.");
            return "redirect:/";
        }
    }

    @GetMapping("/nuevo")
    public String nuevoMantenimiento(Model model,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("mantenimiento", new Mantenimiento());
            return "mantenimiento/form-mantenimiento";

        } catch (Exception e) {
            logger.error("Error en nuevo mantenimiento", e);
            redirect.addFlashAttribute("error", "Error al cargar formulario.");
            return "redirect:/mantenimiento/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarMantenimiento(@RequestParam(required = false) Integer id,
            @RequestParam String tipoMantenimiento,
            @RequestParam String especialidadMantenimiento,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            if (tipoMantenimiento == null || tipoMantenimiento.trim().isEmpty()) {
                throw new CampoObligatorioException("Tipo de mantenimiento");
            }

            if (especialidadMantenimiento == null || especialidadMantenimiento.trim().isEmpty()) {
                throw new CampoObligatorioException("Especialidad de mantenimiento");
            }

            Mantenimiento mantenimiento;

            if (id != null) {
                mantenimiento = mantenimientoService.buscarPorId(id);
                if (mantenimiento == null) {
                    redirect.addFlashAttribute("error", "Mantenimiento no encontrado.");
                    return "redirect:/mantenimiento/listar";
                }
                mantenimiento.setTipoMantenimiento(tipoMantenimiento);
                mantenimiento.setEspecialidadMantenimiento(especialidadMantenimiento);
            } else {
                mantenimiento = new Mantenimiento();
                mantenimiento.setTipoMantenimiento(tipoMantenimiento);
                mantenimiento.setEspecialidadMantenimiento(especialidadMantenimiento);
            }

            Mantenimiento mantenimientoGuardado = mantenimientoService.guardar(mantenimiento);

            if (id == null) {
                redirect.addFlashAttribute("exito", "Mantenimiento registrado correctamente.");
            } else {
                redirect.addFlashAttribute("exito", "Mantenimiento actualizado correctamente.");
            }

            return "redirect:/mantenimiento/listar";

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en guardar mantenimiento: {}", e.getMessage());
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/mantenimiento/nuevo";
        } catch (Exception e) {
            logger.error("Error al guardar mantenimiento - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
            return "redirect:/mantenimiento/listar";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarMantenimiento(@PathVariable Integer id,
            Model model,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
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
            return "mantenimiento/form-mantenimiento";

        } catch (Exception e) {
            logger.error("Error al editar mantenimiento - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al cargar mantenimiento.");
            return "redirect:/mantenimiento/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMantenimiento(@PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            mantenimientoService.eliminar(id);
            redirect.addFlashAttribute("exito", "Mantenimiento eliminado correctamente.");
            return "redirect:/mantenimiento/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar mantenimiento - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
            return "redirect:/mantenimiento/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
