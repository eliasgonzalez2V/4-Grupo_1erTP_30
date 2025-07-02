package proyecto;

import java.time.LocalDate;
import proyecto.Vehiculo;

public class Garage {

    private int nroDeGarage;
    private double contadorLuz;
    private boolean mantenimientoContratado;
    private LocalDate fechaDeAsignacion;
    private int tipoDeRenta;
    private Socio dueno;
    private Vehiculo vehiculo;

    private LocalDate fechaCompra;

    public Garage(int nro, double luz, boolean mantenimientoContratado) {
        this.nroDeGarage = nro;
        this.contadorLuz = luz;
        this.mantenimientoContratado = mantenimientoContratado;
        this.tipoDeRenta = 0;
    }

    public Garage(int nroDeGarage, double contadorLuz, boolean mantenimientoContratado, LocalDate fechaDeAsignacion, int tipoDeRenta, Socio dueño) {
        this.nroDeGarage = nroDeGarage;
        this.contadorLuz = contadorLuz;
        this.mantenimientoContratado = mantenimientoContratado;
        this.fechaDeAsignacion = fechaDeAsignacion;
        this.tipoDeRenta = tipoDeRenta;
        this.dueno = dueno;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public int getNroDeGarage() {
        return nroDeGarage;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public LocalDate getFechaAsignacion() {
        return fechaDeAsignacion;
    }

    public boolean isMantenimientoContratado() {
        return mantenimientoContratado;
    }

    public int getTipoDeRenta() {
        return tipoDeRenta;
    }

    public Socio getDueño() {
        return dueno;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void asignarVehiculo(Vehiculo vehiculo, LocalDate fechaAsignacion) {
        this.vehiculo = vehiculo;
        this.fechaDeAsignacion = fechaAsignacion;
        this.tipoDeRenta = 1;
    }

    public void comprarGarage(Socio s, LocalDate fechaCompra) {
        this.dueno = s;
        this.tipoDeRenta = 2;
    }

    public double getContadorLuz() {
        return contadorLuz;
    }

    public boolean tieneMantenimientoContratado() {
        return mantenimientoContratado;
    }

    public Socio getDueno() {
        return dueno;
    }

}
