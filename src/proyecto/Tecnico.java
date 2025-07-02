package proyecto;

import java.util.ArrayList;
import java.util.Scanner;

public class Tecnico extends Empleado {

    public Tecnico(String DNI, String nombre, String direccion, double telefono, String contrasenia, int codigo, String especialidad) {
        super(DNI, nombre, direccion, telefono, contrasenia, codigo, especialidad);
        Scanner scanner = new Scanner(System.in);
        int opc;

        setCapacidadDeMantenimiento(new CambioDeFrenos());
        setCapacidadDeMantenimiento(new CambioLiquidoDeFrenos());
        setCapacidadDeCargarVehiculo(new IncapacidadCargaDeVehiculo());
        setCapacidadDeLavado(new IncapacidadLavadoDeVehiculo());
    }
}
/*
        do {
            System.out.println("Elija tipo de lavado:\n(1)Cambio de Frenos\n(2)Cambio Liquido de frenos");
            opc = scanner.nextInt();
        } while (opc != 1 || opc != 2);

        if (opc == 1) {
            setCapacidadDeMantenimiento(new CambioDeFrenos());
        } else {
            setCapacidadDeMantenimiento(new CambioLiquidoDeFrenos());
        }
*/
