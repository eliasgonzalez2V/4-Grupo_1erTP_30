package proyecto;

import java.util.ArrayList;

public class Zona {
    private char id;
    private String tipoVehiculos;
    private int cantVehiculos;
    private double profundidadGarage;
    private double anchoGarage;
    private ArrayList<Garage> garages;
    private ArrayList<Empleado> empleados;

    public Zona(char id, String tipoVehiculos, int cantVehiculos, double profundidadGarage, double anchoGarage) {
        this.id = id;
        this.tipoVehiculos = tipoVehiculos;
        this.cantVehiculos = cantVehiculos;
        this.profundidadGarage = profundidadGarage;
        this.anchoGarage = anchoGarage;
        this.garages = new ArrayList<>();
        this.empleados = new ArrayList<>();
    }

    public void agregarGarage(Garage g) {
        garages.add(g);
    }

    public void agregarEmpleado(Empleado e) {
        empleados.add(e);
    }

    public char getId() {
        return id;
    }

    public String getTipoVehiculos() {
        return tipoVehiculos;
    }

    public int getCantVehiculos() {
        return cantVehiculos;
    }

    public double getProfundidadGarage() {
        return profundidadGarage;
    }

    public double getAnchoGarage() {
        return anchoGarage;
    }

    public ArrayList<Garage> getGarages() {
        return garages;
    }

    public ArrayList<Empleado> getEmpleados() {
        return empleados;
    }
}


