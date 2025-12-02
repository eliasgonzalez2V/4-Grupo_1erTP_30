package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Administrador;
import com.proyectofinal.guarderia.guarderia_web.modelos.Empleado;
import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import com.proyectofinal.guarderia.guarderia_web.modelos.dto.LoginForm;
import com.proyectofinal.guarderia.guarderia_web.repositorios.AdministradorRepository;
import com.proyectofinal.guarderia.guarderia_web.repositorios.EmpleadoRepository;
import com.proyectofinal.guarderia.guarderia_web.repositorios.SocioRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/loginForm")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AdministradorRepository administradorRepo;

    @Autowired
    private SocioRepository socioRepo;

    @Autowired
    private EmpleadoRepository empleadoRepo;

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute("loginForm") LoginForm loginForm,
            HttpSession session,
            RedirectAttributes redirect) {
        try {
            Integer dni = loginForm.getDni();
            String contrasenia = loginForm.getContrasenia();

            if (dni == null) {
                redirect.addFlashAttribute("error", "El DNI es obligatorio");
                return "redirect:/loginForm/login";
            }

            if (contrasenia == null || contrasenia.trim().isEmpty()) {
                redirect.addFlashAttribute("error", "La contraseña es obligatoria");
                return "redirect:/loginForm/login";
            }

            Administrador admin = administradorRepo.findByDniAndContrasenia(dni, contrasenia);
            if (admin != null) {
                session.setAttribute("tipo", "administrador");
                session.setAttribute("dniAdministrador", admin.getDni());
                session.setAttribute("nombreAdministrador", admin.getNombre());
                session.setAttribute("idAdministrador", admin.getId());
                return "redirect:/administrador/menu";
            }

            Socio socio = socioRepo.findByDniAndContrasenia(dni, contrasenia);
            if (socio != null) {
                session.setAttribute("tipo", "socio");
                session.setAttribute("dniSocio", socio.getDni());
                session.setAttribute("nombreSocio", socio.getNombre());
                session.setAttribute("idSocio", socio.getId());
                return "redirect:/socio/menu";
            }

            Empleado empleado = empleadoRepo.findByDniAndContrasenia(dni, contrasenia);
            if (empleado != null) {
                session.setAttribute("tipo", "empleado");
                session.setAttribute("dniEmpleado", empleado.getDni());
                session.setAttribute("nombreEmpleado", empleado.getNombre());
                session.setAttribute("idEmpleado", empleado.getId());
                return "redirect:/empleado/menu";
            }

            redirect.addFlashAttribute("error", "DNI o contraseña incorrectos");
            return "redirect:/loginForm/login";

        } catch (Exception e) {
            logger.error("Error en login - DNI: {}", loginForm.getDni(), e);
            redirect.addFlashAttribute("error", "Error temporal en el sistema. Intente nuevamente.");
            return "redirect:/loginForm/login";
        }
    }
}