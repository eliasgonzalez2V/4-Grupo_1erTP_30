package proyecto;

import java.time.LocalDate;

public class Garage {
    private int nroDeGarage;
    private double contadorLuz;
    private boolean mantenimientoContratado;
    private LocalDate fechaDeAsignacion;
    private int tipoDeRenta;
    private Socio dueno;
    private Vehiculo vehiculo;

    public Garage(int nro, double luz, boolean mantenimientoContratado) {
        this.nroDeGarage = nro;
        this.contadorLuz = luz;
        this.mantenimientoContratado = mantenimientoContratado;
        this.tipoDeRenta = 0;
    }

    public void asignarVehiculo(Vehiculo v, LocalDate fecha) {
        this.vehiculo = v;
        this.fechaDeAsignacion = fecha;
        this.tipoDeRenta = 1;
    }

    public void comprarGarage(Socio s, LocalDate fechaCompra) {
        this.dueno = s;
        this.tipoDeRenta = 2;
    }

    public int getNroDeGarage() { 
        return nroDeGarage; 
    }
    
    public double getContadorLuz() { 
        return contadorLuz; 
    }
    
    public boolean tieneMantenimientoContratado() { 
        return mantenimientoContratado; 
    }
    
    public Vehiculo getVehiculo() { 
        return vehiculo; 
    }
    
    public Socio getDueno() { 
        return dueno; 
    }
    
    public int getTipoDeRent() { 
        return tipoDeRenta; 
    }
}