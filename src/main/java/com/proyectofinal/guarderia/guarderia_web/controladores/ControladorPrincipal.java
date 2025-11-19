package com.proyectofinal.guarderia.guarderia_web.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPrincipal {

    @GetMapping("/")
    public String inicio() {
        return "index"; 
    }
}
