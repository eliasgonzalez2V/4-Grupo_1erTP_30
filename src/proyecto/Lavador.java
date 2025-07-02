package proyecto;

import java.util.ArrayList;
import java.util.Scanner;

public class Lavador extends Empleado {

    int opc;

    public Lavador(String DNI, String nombre, String direccion, double telefono, String contrasenia, int codigo, String especialidad) {
        super(DNI, nombre, direccion, telefono, contrasenia, codigo, especialidad);

        Scanner scanner = new Scanner(System.in);
        int opc;
        setCapacidadDeLavado(new LavadoGeneral());
        setCapacidadDeLavado(new LavadoProfundo());
        setCapacidadDeMantenimiento(new IncapacidadMantenimientoDeFrenos());
        setCapacidadDeCargarVehiculo(new IncapacidadCargaDeVehiculo());
    }
}
/*
        do {
            System.out.println("Elija tipo de lavado:\n(1)Lavado general\n(2)Lavado profundo");
            opc = scanner.nextInt();
        } while (opc != 1 || opc != 2);

        if (opc == 1) {
            setCapacidadDeLavado(new LavadoGeneral());
        } else {
            setCapacidadDeLavado(new LavadoProfundo());
        }
*/
