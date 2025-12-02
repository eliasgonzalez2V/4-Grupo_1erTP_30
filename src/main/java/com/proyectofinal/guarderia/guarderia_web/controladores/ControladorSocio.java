package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioService;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioAsignacionService;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
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
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/socio")
public class ControladorSocio {

    private static final Logger logger = LoggerFactory.getLogger(ControladorSocio.class);

    @Autowired
    private SocioService socioService;

    @Autowired
    private SocioAsignacionService socioAsignacionService;

    @Autowired
    private VehiculoService vehiculoService;

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("socio", new Socio());
        return "redirect:/loginForm/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("socio") Socio socio,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            if (result.hasErrors()) {
                return "redirect:/loginForm/login";
            }

            if (socio.getDni() == null) {
                throw new CampoObligatorioException("DNI");
            }

            if (socio.getContrasenia() == null || socio.getContrasenia().trim().isEmpty()) {
                throw new CampoObligatorioException("Contraseña");
            }

            Socio socioLogueado = socioService.login(socio.getDni(), socio.getContrasenia());

            if (socioLogueado != null) {
                session.setAttribute("dniSocio", socioLogueado.getDni());
                session.setAttribute("nombreSocio", socioLogueado.getNombre());
                session.setAttribute("idSocio", socioLogueado.getId());
                return "redirect:/socio/menu";
            } else {
                redirectAttributes.addFlashAttribute("error", "DNI o Contraseña incorrectos.");
                return "redirect:/loginForm/login";
            }

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en login socio: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/loginForm/login";
        } catch (Exception e) {
            logger.error("Error en login socio - DNI: {}", socio.getDni(), e);
            redirectAttributes.addFlashAttribute("error", "Error en el sistema. Intente nuevamente.");
            return "redirect:/loginForm/login";
        }
    }

    @GetMapping("/listar")
    public String listarSocios(HttpSession session, Model model) {
        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("socios", socioService.listarTodos());
            return "socio/listar-socio";

        } catch (Exception e) {
            logger.error("Error en listar socios", e);
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/nuevo")
    public String nuevoSocio(HttpSession session, Model model) {
        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("socio", new Socio());
            return "socio/form-socio";

        } catch (Exception e) {
            logger.error("Error en nuevo socio", e);
            return "redirect:/socio/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarSocio(@Valid @ModelAttribute("socio") Socio socio,
            BindingResult result,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            if (result.hasErrors()) {
                model.addAttribute("error", "Por favor, corrija los errores en el formulario.");
                return "socio/form-socio";
            }

            validarCamposObligatorios(socio);

            if (socio.getFechaIngreso() == null) {
                socio.setFechaIngreso(LocalDate.now());
            }

            socioService.guardar(socio);
            redirectAttributes.addFlashAttribute("success",
                    socio.getId() == null ? "Socio creado correctamente" : "Socio actualizado correctamente");

            return "redirect:/socio/listar";

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en guardar socio: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("socio", socio);
            return "socio/form-socio";
        } catch (Exception e) {
            logger.error("Error al guardar socio - ID: {}", socio.getId(), e);
            model.addAttribute("error", "Error al guardar socio: " + e.getMessage());
            model.addAttribute("socio", socio);
            return "socio/form-socio";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarSocio(@PathVariable Integer id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            Socio socio = socioService.buscarPorId(id);
            if (socio == null) {
                redirectAttributes.addFlashAttribute("error", "Socio no encontrado.");
                return "redirect:/socio/listar";
            }

            model.addAttribute("socio", socio);
            return "socio/form-socio";

        } catch (Exception e) {
            logger.error("Error al editar socio - ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al cargar socio.");
            return "redirect:/socio/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarSocio(@PathVariable Integer id, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            socioService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Socio eliminado correctamente");

            return "redirect:/socio/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar socio - ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar socio: " + e.getMessage());
            return "redirect:/socio/listar";
        }
    }

    @GetMapping("/menu")
    public String menuSocio(HttpSession session, Model model) {
        try {
            if (noHaySesionSocio(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            model.addAttribute("dniSocio", session.getAttribute("dniSocio"));
            return "socio/menu-socio";

        } catch (Exception e) {
            logger.error("Error en menu socio", e);
            return "redirect:/loginForm/login";
        }
    }

    @GetMapping("/mis-datos")
    public String verMisDatos(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (noHaySesionSocio(session)) {
                return "redirect:/loginForm/login";
            }

            Integer socioId = (Integer) session.getAttribute("idSocio");
            Socio socio = socioService.buscarPorId(socioId);

            if (socio == null) {
                redirectAttributes.addFlashAttribute("error", "Socio no encontrado.");
                return "redirect:/socio/menu";
            }

            model.addAttribute("socio", socio);
            model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            return "socio/ver-datos-socio";

        } catch (Exception e) {
            logger.error("Error en mis datos socio - ID: {}", session.getAttribute("idSocio"), e);
            redirectAttributes.addFlashAttribute("error", "Error al cargar datos.");
            return "redirect:/socio/menu";
        }
    }

    @GetMapping("/mis-garages")
    public String verMisGarages(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (noHaySesionSocio(session)) {
                return "redirect:/loginForm/login";
            }

            Integer socioId = (Integer) session.getAttribute("idSocio");

            List<Garage> garagesPropios = socioAsignacionService.getGaragesPropiosPorSocio(socioId);
            List<Garage> garagesRentados = socioAsignacionService.getGaragesRentadosPorSocio(socioId);
            List<Vehiculo> misVehiculos = vehiculoService.buscarVehiculosPorSocioId(socioId);

            model.addAttribute("nombreSocio", session.getAttribute("nombreSocio"));
            model.addAttribute("garagesPropios", garagesPropios);
            model.addAttribute("garagesRentados", garagesRentados);
            model.addAttribute("misVehiculos", misVehiculos);

            return "socio/mis-garages-socio";

        } catch (Exception e) {
            logger.error("Error en mis garages socio - ID: {}", session.getAttribute("idSocio"), e);
            redirectAttributes.addFlashAttribute("error", "Error al cargar garages: " + e.getMessage());
            return "redirect:/socio/menu";
        }
    }

    @PostMapping("/asignar-vehiculo")
    public String asignarVehiculoGarage(
            @RequestParam Integer garageId,
            @RequestParam Integer vehiculoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            if (noHaySesionSocio(session)) {
                return "redirect:/loginForm/login";
            }

            Integer socioId = (Integer) session.getAttribute("idSocio");

            boolean exito = socioAsignacionService.socioAsignaVehiculoGaragePropio(socioId, garageId, vehiculoId);

            if (exito) {
                redirectAttributes.addFlashAttribute("exito", "Vehículo asignado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Error al asignar vehículo.");
            }

            return "redirect:/socio/mis-garages";

        } catch (Exception e) {
            logger.error("Error al asignar vehículo a garage propio - Socio: {}, Garage: {}, Vehículo: {}",
                    session.getAttribute("idSocio"), garageId, vehiculoId, e);
            redirectAttributes.addFlashAttribute("error", "Error en la asignación: " + e.getMessage());
            return "redirect:/socio/mis-garages";
        }
    }

    @PostMapping("/asignar-vehiculo-rentado")
    public String asignarVehiculoGarageRentado(
            @RequestParam Integer garageId,
            @RequestParam Integer vehiculoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            if (noHaySesionSocio(session)) {
                return "redirect:/loginForm/login";
            }

            Integer socioId = (Integer) session.getAttribute("idSocio");

            boolean exito = socioAsignacionService.adminAsignaVehiculoGarageRentado(socioId, garageId, vehiculoId);

            if (exito) {
                redirectAttributes.addFlashAttribute("exito", "Vehículo asignado correctamente al garage rentado.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Error al asignar vehículo.");
            }

            return "redirect:/socio/mis-garages";

        } catch (Exception e) {
            logger.error("Error al asignar vehículo a garage rentado - Socio: {}, Garage: {}, Vehículo: {}",
                    session.getAttribute("idSocio"), garageId, vehiculoId, e);
            redirectAttributes.addFlashAttribute("error", "Error en la asignación: " + e.getMessage());
            return "redirect:/socio/mis-garages";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private boolean noHaySesionSocio(HttpSession session) {
        return session.getAttribute("dniSocio") == null;
    }

    private boolean noHaySesionAdmin(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    private void validarCamposObligatorios(Socio socio) {
        if (socio.getDni() == null) {
            throw new CampoObligatorioException("DNI");
        }
        if (socio.getNombre() == null || socio.getNombre().trim().isEmpty()) {
            throw new CampoObligatorioException("Nombre");
        }
        if (socio.getDireccion() == null || socio.getDireccion().trim().isEmpty()) {
            throw new CampoObligatorioException("Dirección");
        }
        if (socio.getTelefono() == null || socio.getTelefono().trim().isEmpty()) {
            throw new CampoObligatorioException("Teléfono");
        }
        if (socio.getId() == null && (socio.getContrasenia() == null || socio.getContrasenia().trim().isEmpty())) {
            throw new CampoObligatorioException("Contraseña");
        }
    }
}
