package com.proyectofinal.guarderia.guarderia_web.excepciones;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ManejadorGlobalExcepciones {

    private static final Logger logger = LoggerFactory.getLogger(ManejadorGlobalExcepciones.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String manejarDataIntegrityViolation(DataIntegrityViolationException e,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        logger.error("Error de integridad de datos: {}", e.getMessage());

        String mensaje = "Error en la operación. Verifique los datos ingresados.";
        if (e.getMessage().contains("foreign key") || e.getMessage().contains("constraint")) {
            mensaje = "No se puede completar la operación porque hay elementos relacionados.";
        } else if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
            mensaje = "Ya existe un registro con los mismos datos.";
        }

        redirectAttributes.addFlashAttribute("error", mensaje);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String manejarDataAccess(DataAccessException e,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        logger.error("Error de acceso a datos: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", "Error en la base de datos.");

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(CampoObligatorioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String manejarCampoObligatorio(CampoObligatorioException e,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        logger.warn("Campo obligatorio no completado: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @ExceptionHandler(ValidacionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String manejarValidacion(ValidacionException e,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        logger.warn("Error de validación: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("error", e.getMessage());

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}