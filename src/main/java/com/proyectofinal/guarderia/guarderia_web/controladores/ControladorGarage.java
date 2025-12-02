package com.proyectofinal.guarderia.guarderia_web.controladores;

import com.proyectofinal.guarderia.guarderia_web.modelos.Garage;
import com.proyectofinal.guarderia.guarderia_web.modelos.Vehiculo;
import com.proyectofinal.guarderia.guarderia_web.modelos.Socio;
import com.proyectofinal.guarderia.guarderia_web.servicios.GarageService;
import com.proyectofinal.guarderia.guarderia_web.servicios.ZonaService;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioService;
import com.proyectofinal.guarderia.guarderia_web.servicios.VehiculoService;
import com.proyectofinal.guarderia.guarderia_web.servicios.SocioAsignacionService;
import com.proyectofinal.guarderia.guarderia_web.excepciones.CampoObligatorioException;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/garage")
public class ControladorGarage {

    private static final Logger logger = LoggerFactory.getLogger(ControladorGarage.class);

    @Autowired
    private GarageService garageService;

    @Autowired
    private ZonaService zonaService;

    @Autowired
    private SocioService socioService;

    @Autowired
    private VehiculoService vehiculoService;

    @Autowired
    private SocioAsignacionService socioAsignacionService;

    private boolean adminNoLogueado(HttpSession session) {
        return session.getAttribute("dniAdministrador") == null;
    }

    @GetMapping("/listar")
    public String listarGarages(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("garages", garageService.listarTodos());
            return "garage/listar-garage";

        } catch (Exception e) {
            logger.error("Error en listar garages", e);
            redirect.addFlashAttribute("error", "Error al cargar garages.");
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/nuevo")
    public String nuevoGarage(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("garage", new Garage());
            model.addAttribute("zonas", zonaService.listarTodos());

            Iterable<Socio> sociosIterable = socioService.listarTodos();
            List<Socio> sociosList = new ArrayList<>();
            sociosIterable.forEach(sociosList::add);
            model.addAttribute("socios", sociosList);

            return "garage/form-garage";

        } catch (Exception e) {
            logger.error("Error en nuevo garage", e);
            redirect.addFlashAttribute("error", "Error al cargar formulario.");
            return "redirect:/garage/listar";
        }
    }

    @GetMapping("/gestion-rentas")
    public String gestionRentas(Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            Iterable<Socio> sociosIterable = socioService.listarTodos();
            List<Socio> sociosList = new ArrayList<>();
            sociosIterable.forEach(sociosList::add);

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("socios", sociosList);
            return "administrador/gestion-rentas-admin";

        } catch (Exception e) {
            logger.error("Error en gestión rentas", e);
            redirect.addFlashAttribute("error", "Error al cargar gestión de rentas.");
            return "redirect:/administrador/menu";
        }
    }

    @GetMapping("/datos-socio/{socioId}")
    public String obtenerDatosSocio(@PathVariable Integer socioId, Model model, HttpSession session) {
        try {
            if (session.getAttribute("dniAdministrador") == null) {
                model.addAttribute("error", "Sesión no válida");
                return "administrador/datos-socio-rentas :: datosSocio";
            }

            Socio socio = socioService.buscarPorId(socioId);
            if (socio != null) {
                List<Garage> garagesRentados = socioAsignacionService.getGaragesRentadosPorSocio(socioId);
                List<Garage> garagesPropios = socioAsignacionService.getGaragesPropiosPorSocio(socioId);
                List<Vehiculo> vehiculosSocio = socioAsignacionService.getVehiculosPorSocio(socioId);

                model.addAttribute("socio", socio);
                model.addAttribute("garagesRentados", garagesRentados);
                model.addAttribute("garagesPropios", garagesPropios);
                model.addAttribute("vehiculosSocio", vehiculosSocio);
            }

            return "administrador/datos-socio-rentas :: datosSocio";

        } catch (Exception e) {
            logger.error("Error en datos socio - ID: {}", socioId, e);
            model.addAttribute("error", "Error al cargar datos del socio");
            return "administrador/datos-socio-rentas :: datosSocio";
        }
    }

    @PostMapping("/asignar-vehiculo")
    public String asignarVehiculoGarage(
            @RequestParam Integer socioId,
            @RequestParam Integer garageId,
            @RequestParam Integer vehiculoId,
            @RequestParam String tipoGarage,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión como administrador.");
                return "redirect:/";
            }

            if (vehiculoId == 0) {
                boolean exitoVaciar;

                if ("propio".equals(tipoGarage)) {
                    exitoVaciar = socioAsignacionService.vaciarGaragePropio(socioId, garageId);
                } else {
                    exitoVaciar = socioAsignacionService.vaciarGarageRentado(socioId, garageId);
                }

                if (exitoVaciar) {
                    redirect.addFlashAttribute("exito", "El garage fue dejado vacío correctamente.");
                } else {
                    redirect.addFlashAttribute("error", "No se pudo dejar el garage vacío.");
                }

                return "redirect:/garage/gestion-rentas";
            }

            boolean exito;

            if ("propio".equals(tipoGarage)) {
                exito = socioAsignacionService.adminAsignaVehiculoGaragePropio(socioId, garageId, vehiculoId);
            } else {
                exito = socioAsignacionService.adminAsignaVehiculoGarageRentado(socioId, garageId, vehiculoId);
            }

            if (exito) {
                redirect.addFlashAttribute("exito", "Vehículo asignado correctamente al garage.");
            } else {
                redirect.addFlashAttribute("error", "Error en la asignación.");
            }

            return "redirect:/garage/gestion-rentas";

        } catch (Exception e) {
            logger.error("Error en asignar vehículo - Socio: {}, Garage: {}, Vehículo: {}", 
                        socioId, garageId, vehiculoId, e);
            redirect.addFlashAttribute("error", "Error en la asignación: " + e.getMessage());
            return "redirect:/garage/gestion-rentas";
        }
    }

    @PostMapping("/guardar")
    public String guardarGarage(@ModelAttribute Garage garage,
            @RequestParam(value = "socioRentado.id", required = false) Integer socioRentadoId,
            HttpSession session,
            RedirectAttributes redirect) {

        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            validarCamposObligatoriosGarage(garage);

            Integer garageId = garage.getId();

            if (garageId != null) {
                Garage garageExistente = garageService.buscarPorId(garageId);
                if (garageExistente != null) {
                    garage.setAsignacionesSocio(garageExistente.getAsignacionesSocio());
                    garage.setVehiculoAsignado(garageExistente.getVehiculoAsignado());

                    if (garage.getFechaInicioRenta() == null) {
                        garage.setFechaInicioRenta(garageExistente.getFechaInicioRenta());
                    }
                    if (garage.getFechaFinRenta() == null) {
                        garage.setFechaFinRenta(garageExistente.getFechaFinRenta());
                    }
                    if (garage.getFechaCompra() == null) {
                        garage.setFechaCompra(garageExistente.getFechaCompra());
                    }
                    if (garage.getContadorLuz() == null) {
                        garage.setContadorLuz(garageExistente.getContadorLuz());
                    }
                }
            }

            if (socioRentadoId != null) {
                Socio socio = socioService.buscarPorId(socioRentadoId);
                garage.setSocioRentado(socio);
            } else {
                garage.setSocioRentado(null);
            }

            boolean seleccionoComprado
                    = (garage.getSocioPropietario() != null && garage.getSocioPropietario().getId() != null)
                    || garage.getFechaCompra() != null;

            boolean seleccionoRentado
                    = socioRentadoId != null
                    || garage.getFechaInicioRenta() != null
                    || garage.getFechaFinRenta() != null;

            if (seleccionoComprado && seleccionoRentado) {
                redirect.addFlashAttribute("error", "Debe elegir sólo una sección: comprado o rentado.");
                return garageId == null ? "redirect:/garage/nuevo" : "redirect:/garage/editar/" + garageId;
            }

            if (seleccionoComprado) {
                if (garage.getSocioPropietario() == null || garage.getFechaCompra() == null) {
                    redirect.addFlashAttribute("error", "Si completa la sección comprado, debe ingresar socio propietario y fecha de compra.");
                    return garageId == null ? "redirect:/garage/nuevo" : "redirect:/garage/editar/" + garageId;
                }

                garage.setSocioRentado(null);
                garage.setFechaInicioRenta(null);
                garage.setFechaFinRenta(null);
            }

            if (seleccionoRentado) {
                if (socioRentadoId == null
                        || garage.getFechaInicioRenta() == null
                        || garage.getFechaFinRenta() == null) {

                    redirect.addFlashAttribute("error", "Si completa la sección rentado, debe ingresar socio y fechas de renta.");
                    return garageId == null ? "redirect:/garage/nuevo" : "redirect:/garage/editar/" + garageId;
                }

                Socio socio = socioService.buscarPorId(socioRentadoId);
                garage.setSocioRentado(socio);

                garage.setSocioPropietario(null);
                garage.setFechaCompra(null);
            }

            if (!seleccionoComprado && !seleccionoRentado) {
                garage.setSocioPropietario(null);
                garage.setFechaCompra(null);
                garage.setSocioRentado(null);
                garage.setFechaInicioRenta(null);
                garage.setFechaFinRenta(null);
            }

            garageService.guardar(garage);
            redirect.addFlashAttribute("exito", "Garage guardado correctamente.");
            return "redirect:/garage/listar";

        } catch (CampoObligatorioException e) {
            logger.error("Error campo obligatorio en guardar garage: {}", e.getMessage());
            redirect.addFlashAttribute("error", e.getMessage());
            Integer garageId = garage.getId();
            if (garageId == null) {
                return "redirect:/garage/nuevo";
            } else {
                return "redirect:/garage/editar/" + garageId;
            }
        } catch (Exception e) {
            logger.error("Error al guardar garage - ID: {}", garage.getId(), e);
            redirect.addFlashAttribute("error", "Error al guardar el garage: " + e.getMessage());
            Integer garageId = garage.getId();
            if (garageId == null) {
                return "redirect:/garage/nuevo";
            } else {
                return "redirect:/garage/editar/" + garageId;
            }
        }
    }

    @GetMapping("/editar/{id}")
    public String editarGarage(@PathVariable Integer id, Model model, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            Garage garage = garageService.buscarPorId(id);

            if (garage == null) {
                redirect.addFlashAttribute("error", "El garage no existe.");
                return "redirect:/garage/listar";
            }

            model.addAttribute("nombreAdministrador", session.getAttribute("nombreAdministrador"));
            model.addAttribute("garage", garage);
            model.addAttribute("zonas", zonaService.listarTodos());

            Iterable<Socio> sociosIterable = socioService.listarTodos();
            List<Socio> sociosList = new ArrayList<>();
            sociosIterable.forEach(sociosList::add);
            model.addAttribute("socios", sociosList);

            return "garage/form-garage";

        } catch (Exception e) {
            logger.error("Error al editar garage - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al cargar el garage: " + e.getMessage());
            return "redirect:/garage/listar";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarGarage(@PathVariable Integer id, HttpSession session, RedirectAttributes redirect) {
        try {
            if (adminNoLogueado(session)) {
                redirect.addFlashAttribute("error", "Debe iniciar sesión.");
                return "redirect:/";
            }

            garageService.eliminar(id);
            redirect.addFlashAttribute("exito", "Garage eliminado correctamente.");
            return "redirect:/garage/listar";

        } catch (Exception e) {
            logger.error("Error al eliminar garage - ID: {}", id, e);
            redirect.addFlashAttribute("error", "Error al eliminar el garage: " + e.getMessage());
            return "redirect:/garage/listar";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    private void validarCamposObligatoriosGarage(Garage garage) {
        if (garage.getZona() == null || garage.getZona().getId() == null) {
            throw new CampoObligatorioException("Zona");
        }
    }
}