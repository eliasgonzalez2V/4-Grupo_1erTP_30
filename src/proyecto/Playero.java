package proyecto;

import java.util.ArrayList;
import java.util.Scanner;

public class Playero extends Empleado {

    public Playero(String DNI, String nombre, String direccion, double telefono, String contrasenia, int codigo, String especialidad) {
        super(DNI, nombre, direccion, telefono, contrasenia, codigo, especialidad);

        Scanner scanner = new Scanner(System.in);
        int opc;

        setCapacidadDeCargarVehiculo(new CargaBateria());
        setCapacidadDeCargarVehiculo(new CargaDeTanque());
        setCapacidadDeMantenimiento(new IncapacidadMantenimientoDeFrenos());
        setCapacidadDeLavado(new IncapacidadLavadoDeVehiculo());
    }
}
/*
        do {
            System.out.println("Elija tipo de lavado:\n(1)Cargar Bateria\n(2)Cargar Tanque");
            opc = scanner.nextInt();
        } while (opc != 1 || opc != 2);

        if (opc == 1) {
            setCapacidadDeCargarVehiculo(new CargaBateria());
        } else {
            setCapacidadDeCargarVehiculo(new CargaDeTanque());
        }
*/
