package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Empleado;
import com.proyectofinal.guarderia.guarderia_web.modelos.Especialidad;
import com.proyectofinal.guarderia.guarderia_web.servicios.EmpleadoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.EspecialidadService;
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
@RequestMapping("/empleado")
public class ControladorEmpleado {

    private static final Logger logger = LoggerFactory.getLogger(ControladorEmpleado.class);

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "redirect:/loginForm/login";
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("empleado") Empleado empleado,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            if (result.hasErrors()) {
                return "redirect:/loginForm/login";
            }

            if (empleado.getDni() == null) {
                throw new CampoObligatorioException("DNI");
            }

            if (empleado.getContrasenia() == null || empleado.getContrasenia().trim().isEmpty()) {
                throw new CampoObligatorioException("Contraseña");
            }

            Empleado empLogueado = empleadoService.login(empleado.getDni(), empleado.getContrasenia());

            if (empLogueado != null) {
                session.setAttribute("dniEmpleado", empLogueado.getDni());
                session.setAttribute("nombreEmpleado", empLogueado.getNombre());
                session.setAttribute("idEmpleado", empLogueado.getId());
                return "redirect:/empleado/menu";
            } else {
                redirectAttributes.addFlashAttribute("error", "DNI o contraseña incorrectos.");
                return "redirect:/loginForm/login";
            }

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en login empleado: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/loginForm/login";
        } catch (Exception e) {
            logger.error("Error en login empleado - DNI: {}", empleado.getDni(), e);
            redirectAttributes.addFlashAttribute("error", "Error en el sistema. Intente nuevamente.");
            return "redirect:/loginForm/login";
        }
    }

    private boolean noHaySesionAdmin(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/menu")
    public String menuEmpleado(HttpSession session, Model model) {
        try {
            Object dniEmpleado = session.getAttribute("dniEmpleado");
            if (dniEmpleado == null) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("nombreEmpleado", session.getAttribute("nombreEmpleado"));
            model.addAttribute("dniEmpleado", dniEmpleado);
            return "empleado/menu-empleado";

        } catch (Exception e) {
            logger.error("Error en menu empleado", e);
            return "redirect:/loginForm/login";
        }
    }

    @GetMapping("/mis-datos")
    public String verMisDatos(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object dniEmpleado = session.getAttribute("dniEmpleado");
            if (dniEmpleado == null) {
                return "redirect:/loginForm/login";
            }

            Integer empleadoId = (Integer) session.getAttribute("idEmpleado");
            Empleado empleado = empleadoService.buscarPorId(empleadoId);

            if (empleado == null) {
                redirectAttributes.addFlashAttribute("error", "Empleado no encontrado.");
                return "redirect:/empleado/menu";
            }

            model.addAttribute("empleado", empleado);
            model.addAttribute("nombreEmpleado", session.getAttribute("nombreEmpleado"));
            return "empleado/ver-datos-empleado";

        } catch (Exception e) {
            logger.error("Error en mis datos empleado - ID: {}", session.getAttribute("idEmpleado"), e);
            redirectAttributes.addFlashAttribute("error", "Error al cargar datos.");
            return "redirect:/empleado/menu";
        }
    }

    @GetMapping("/listar")
    public String listarEmpleados(HttpSession session, Model model) {
        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("empleados", empleadoService.listarTodos());
            return "empleado/listar-empleado";

        } catch (Exception e) {
            logger.error("Error en listar empleados", e);
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/nuevo")
    public String nuevoEmpleado(HttpSession session, Model model) {
        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            model.addAttribute("empleado", new Empleado());
            model.addAttribute("especialidades", especialidadService.listarTodos());
            return "empleado/form-empleado";

        } catch (Exception e) {
            logger.error("Error en nuevo empleado", e);
            return "redirect:/empleado/listar";
        }
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@Valid @ModelAttribute("empleado") Empleado empleado,
            BindingResult result,
            @RequestParam(value = "especialidad.id", required = false) Integer especialidadId,
            RedirectAttributes redirectAttributes,
            HttpSession session,
            Model model) {

        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            if (result.hasErrors()) {
                model.addAttribute("especialidades", especialidadService.listarTodos());
                model.addAttribute("error", "Por favor, corrija los errores en el formulario.");
                return "empleado/form-empleado";
            }

            if (especialidadId == null) {
                model.addAttribute("especialidades", especialidadService.listarTodos());
                model.addAttribute("error", "Debe seleccionar una especialidad.");
                model.addAttribute("empleado", empleado);
                return "empleado/form-empleado";
            }

            validarCamposObligatorios(empleado);

            Especialidad especialidad = especialidadService.buscarPorId(especialidadId);
            if (especialidad == null) {
                model.addAttribute("especialidades", especialidadService.listarTodos());
                model.addAttribute("error", "La especialidad seleccionada no existe.");
                model.addAttribute("empleado", empleado);
                return "empleado/form-empleado";
            }
            empleado.setEspecialidad(especialidad);

            empleadoService.guardar(empleado);
            redirectAttributes.addFlashAttribute("success",
                    empleado.getId() == null ? "Empleado creado correctamente" : "Empleado actualizado correctamente");

            return "redirect:/empleado/listar";

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en guardar empleado: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("empleado", empleado);
            model.addAttribute("especialidades", especialidadService.listarTodos());
            return "empleado/form-empleado";
        } catch (Exception e) {
            logger.error("Error al guardar empleado - ID: {}", empleado.getId(), e);
            model.addAttribute("error", "Error al guardar empleado: " + e.getMessage());
            model.addAttribute("empleado", empleado);
            model.addAttribute("especialidades", especialidadService.listarTodos());
            return "empleado/form-empleado";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable Integer id,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            Empleado empleado = empleadoService.buscarPorId(id);
            if (empleado == null) {
                redirectAttributes.addFlashAttribute("error", "Empleado no encontrado.");
                return "redirect:/empleado/listar";
            }

            model.addAttribute("empleado", empleado);
            model.addAttribute("especialidades", especialidadService.listarTodos());
            return "empleado/form-empleado";

        } catch (Exception e) {
            logger.error("Error al editar empleado - ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al cargar empleado.");
            return "redirect:/empleado/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        try {
            if (noHaySesionAdmin(session)) {
                return "redirect:/loginForm/login";
            }

            empleadoService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado correctamente");
            return "redirect:/empleado/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar empleado - ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error al eliminar empleado: " + e.getMessage());
            return "redirect:/empleado/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private void validarCamposObligatorios(Empleado empleado) {
        if (empleado.getDni() == null) {
            throw new CampoObligatorioException("DNI");
        }
        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            throw new CampoObligatorioException("Nombre");
        }
        if (empleado.getDireccion() == null || empleado.getDireccion().trim().isEmpty()) {
            throw new CampoObligatorioException("Dirección");
        }
        if (empleado.getTelefono() == null || empleado.getTelefono().trim().isEmpty()) {
            throw new CampoObligatorioException("Teléfono");
        }
        if (empleado.getId() == null && (empleado.getContrasenia() == null || empleado.getContrasenia().trim().isEmpty())) {
            throw new CampoObligatorioException("Contraseña");
        }
    }
}
