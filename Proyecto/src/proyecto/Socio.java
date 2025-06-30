package proyecto;

import java.time.LocalDate;
import java.util.ArrayList;

public class Socio extends Persona {
    private LocalDate fechaDeIngreso;
    private ArrayList<Vehiculo> vehiculos;

    public Socio(String DNI, String nombre, String direccion, double telefono, String contrasenia, LocalDate fechaDeIngreso) {
        super(DNI, nombre, direccion, telefono, contrasenia);
        setpersMenu(new MenuSocio());
        this.fechaDeIngreso = fechaDeIngreso;
        this.vehiculos = new ArrayList<>();
    }

    public LocalDate getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    public void agregarVehiculo(Vehiculo v) {
        vehiculos.add(v);
    }

    public ArrayList<Vehiculo> getVehiculos() {
        return vehiculos;
    }
}

