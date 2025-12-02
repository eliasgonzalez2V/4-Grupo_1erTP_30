package com.proyectofinal.guarderia.guarderia_web.modelos.dto;

public class MantenimientoSocioDTO {
    private Integer id;
    private String tipoMantenimiento;
    private String especialidadMantenimiento;
    private String vehiculoMatricula;
    private String vehiculoMarca;

    public MantenimientoSocioDTO() {
    }

    public MantenimientoSocioDTO(Integer id, String tipoMantenimiento, 
                                String especialidadMantenimiento, String vehiculoMatricula, String vehiculoMarca) {
        this.id = id;
        this.tipoMantenimiento = tipoMantenimiento;
        this.especialidadMantenimiento = especialidadMantenimiento;
        this.vehiculoMatricula = vehiculoMatricula;
        this.vehiculoMarca = vehiculoMarca;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(String tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public String getEspecialidadMantenimiento() {
        return especialidadMantenimiento;
    }

    public void setEspecialidadMantenimiento(String especialidadMantenimiento) {
        this.especialidadMantenimiento = especialidadMantenimiento;
    }

    public String getVehiculoMatricula() {
        return vehiculoMatricula;
    }

    public void setVehiculoMatricula(String vehiculoMatricula) {
        this.vehiculoMatricula = vehiculoMatricula;
    }

    public String getVehiculoMarca() {
        return vehiculoMarca;
    }

    public void setVehiculoMarca(String vehiculoMarca) {
        this.vehiculoMarca = vehiculoMarca;
    }
}