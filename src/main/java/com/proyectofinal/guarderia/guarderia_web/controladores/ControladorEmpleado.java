package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Empleado;
import com.proyectofinal.guarderia.guarderia_web.modelos.Especialidad;
import com.proyectofinal.guarderia.guarderia_web.servicios.EmpleadoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.EspecialidadService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/empleado")
public class ControladorEmpleado {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private EspecialidadService especialidadService;

    // ---------------------------------------------------
    // LOGIN
    // ---------------------------------------------------

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "login-empleado";
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("empleado") Empleado empleado,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                HttpSession session) {

        if (result.hasErrors()) {
            return "login-empleado";
        }

        Empleado empLogueado = empleadoService.login(empleado.getDni(), empleado.getContrasenia());

        if (empLogueado != null) {

            session.setAttribute("dniEmpleado", empLogueado.getDni());
            session.setAttribute("nombreEmpleado", empLogueado.getNombre());
            session.setAttribute("idEmpleado", empLogueado.getId());

            return "redirect:/empleado/menu";
        } else {
            redirectAttributes.addFlashAttribute("error", "DNI o contraseña incorrectos.");
            return "redirect:/empleado/login";
        }
    }
        // ---------------------------------------------------
        // VERIFICAR SESIÓN (ADMIN O EMPLEADO)
        // ---------------------------------------------------
        private boolean noHaySesion(HttpSession session) {
            return session.getAttribute("dniEmpleado") == null 
                && session.getAttribute("dniAdministrador") == null;
        }


    // ---------------------------------------------------
    // VERIFICAR SESIÓN (EMPLEADO)
    // ---------------------------------------------------
    private boolean noHaySesionEmpleado(HttpSession session) {
        return session.getAttribute("dniEmpleado") == null;
    }
    // ---------------------------------------------------
    // VERIFICAR SESIÓN (ADMIN )
    // ---------------------------------------------------
    private boolean noHaySesionAdmin(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }


    // ---------------------------------------------------
    // MENÚ (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/menu")
    public String menuEmpleado(HttpSession session, Model model) {

        if (noHaySesionEmpleado(session)) return "redirect:/empleado/login";

        model.addAttribute("nombreEmpleado", session.getAttribute("nombreEmpleado"));
        model.addAttribute("dniEmpleado", session.getAttribute("dniEmpleado"));

        return "menu-empleado";
    }

    // ---------------------------------------------------
    // CRUD EMPLEADOS (PROTEGIDO)
    // ---------------------------------------------------

    @GetMapping("/listar")
    public String listarEmpleados(HttpSession session, Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        model.addAttribute("empleados", empleadoService.listarTodos());
        return "listar-empleado";
    }

    @GetMapping("/nuevo")
    public String nuevoEmpleado(HttpSession session, Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        model.addAttribute("empleado", new Empleado());
        model.addAttribute("especialidades", especialidadService.listarTodos());
        return "form-empleado";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@Valid @ModelAttribute("empleado") Empleado empleado,
                                  BindingResult result,
                                  @RequestParam("especialidad.id") Integer especialidadId,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession session) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Revisá los datos ingresados.");
            return "redirect:/empleado/nuevo";
        }

        try {
            Especialidad especialidad = especialidadService.buscarPorId(especialidadId);
            empleado.setEspecialidad(especialidad);

            empleadoService.guardar(empleado);
            redirectAttributes.addFlashAttribute("success", "Empleado guardado correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar empleado: " + e.getMessage());
        }

        return "redirect:/empleado/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable Integer id,
                                 HttpSession session,
                                 Model model) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        model.addAttribute("empleado", empleadoService.buscarPorId(id));
        model.addAttribute("especialidades", especialidadService.listarTodos());
        return "form-empleado";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        if (noHaySesionAdmin(session)) return "redirect:/";

        try {
            empleadoService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar empleado: " + e.getMessage());
        }

        return "redirect:/empleado/listar";
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
