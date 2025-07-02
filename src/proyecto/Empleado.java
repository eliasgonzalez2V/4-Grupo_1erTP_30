package proyecto;

import java.util.ArrayList;

public abstract class Empleado extends Persona {

    private int codigo;
    private String especialidad;
    private ArrayList<Vehiculo> vehiculosAsignados;
    private ArrayList<Zona> zonasAsignadas;

    private ArrayList<LavadoDeVehiculo> capacidadDeLavado = new ArrayList<>();
    private ArrayList<MantenimientoDeFrenos> capacidadDeMantenimiento = new ArrayList<>();
    private ArrayList<CargaDeVehiculo> capacidadDeCargarVehiculo = new ArrayList<>();

    public Empleado(String DNI, String nombre, String direccion, double telefono, String contrasenia, int codigo, String especialidad) {
        super(DNI, nombre, direccion, telefono, contrasenia);
        this.codigo = codigo;
        this.especialidad = especialidad;
        vehiculosAsignados = new ArrayList();
        zonasAsignadas = new ArrayList();
    }

    public void asignarZona(Zona zona) {
        if (!zonasAsignadas.contains(zona)) {
            zonasAsignadas.add(zona);
        }
    }

    public void setZonasAsignadas(ArrayList<Zona> zonasAsignadas) {
        this.zonasAsignadas = zonasAsignadas;
    }

    @Override
    public String toString() {
        StringBuilder datos = new StringBuilder();
        datos.append("Codigo: ").append(codigo).append("\nEspecialidad: ").append(especialidad).append("\nVehiculos asignados: ");

        for (Vehiculo v : vehiculosAsignados) {
            datos.append("\n").append(v.getMatricula());
        }
        return datos.toString();
    }

    public ArrayList<Zona> getZonasAsignadas() {
        return zonasAsignadas;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public ArrayList<Vehiculo> getVehiculosAsignados() {
        return vehiculosAsignados;
    }

    public void asignarCantidadVehiculos(Zona zona, int cantidad) {
        if (zonasAsignadas.contains(zona)) {
            zona.setCantVehiculos(cantidad);
        } else {
            System.out.println("Zona no asignada al empleado.");
        }
    }

    public void lavarVehiculo() {
        for (LavadoDeVehiculo lavado : capacidadDeLavado) {
            lavado.lavarVehiculo();
        }
    }

    public void mantenerVehiculo() {
        for (MantenimientoDeFrenos matenFrenos : capacidadDeMantenimiento) {
            matenFrenos.mantenimientoFrenos();
        }
    }

    public void cargarVehiculo() {
        for (CargaDeVehiculo cargaVehiculo : capacidadDeCargarVehiculo) {
            cargaVehiculo.cargarVehiculo();
        }
    }
    
    public void lavarVehiculo(Vehiculo vehiculo) {
        for (LavadoDeVehiculo lavado : capacidadDeLavado) {
            lavado.lavarVehiculo();
        }
    }

    public void mantenerVehiculo(Vehiculo vehiculo) {
        for (MantenimientoDeFrenos matenFrenos : capacidadDeMantenimiento) {
            matenFrenos.mantenimientoFrenos();
        }
    }

    public void cargarVehiculo(Vehiculo vehiculo) {
        for (CargaDeVehiculo cargaVehiculo : capacidadDeCargarVehiculo) {
            cargaVehiculo.cargarVehiculo();
        }
    }

    public void setCapacidadDeLavado(LavadoDeVehiculo capacidadDeLavado) {
        this.capacidadDeLavado.add(capacidadDeLavado);
    }

    public void setCapacidadDeMantenimiento(MantenimientoDeFrenos capacidadDeMantenimiento) {
        this.capacidadDeMantenimiento.add(capacidadDeMantenimiento);
    }

    public void setCapacidadDeCargarVehiculo(CargaDeVehiculo capacidadDeCargarVehiculo) {
        this.capacidadDeCargarVehiculo.add(capacidadDeCargarVehiculo);
    }

}
