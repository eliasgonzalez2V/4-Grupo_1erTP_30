package proyecto;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuEmpleado implements Menu {

    @Override
    public void mostrar() {
        ArrayList<Zona> zonas = BaseDeDatos.zonas;
        Empleado empleado = BaseDeDatos.empleadoActual;
        Scanner input = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- Menú Empleado ---");
            System.out.println("1. Ver datos personales");
            System.out.println("2. Ver zonas asignadas");
            System.out.println("3. Ver vehículos asignados por zona");
            System.out.println("4. Marcar vehículo en el cual se travajo.");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = input.nextInt();
            input.nextLine();

            switch (opcion) {
                case 1 ->
                    mostrarDatos(empleado);
                case 2 ->
                    mostrarZonasAsignadas(empleado);
                case 3 ->
                    mostrarVehiculosPorZona(empleado, zonas);
                case 4 ->
                    asignarTrabajoVehiculo(empleado);
                case 0 ->
                    System.out.println("Saliendo del menú empleado.");
                default ->
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    private void mostrarDatos(Empleado empleado) {
        System.out.println("Nombre: " + empleado.getNombre());
        System.out.println("DNI: " + empleado.getDNI());
        System.out.println("Dirección: " + empleado.getDireccion());
        System.out.println("Teléfono: " + empleado.getTelefono());
        System.out.println("Especialidad: " + empleado.getEspecialidad());
    }

    private void mostrarZonasAsignadas(Empleado empleado) {
        if (empleado.getZonasAsignadas().isEmpty()) {
            System.out.println("No tiene zonas asignadas.");
            return;
        }
        System.out.println("Zonas asignadas:");
        for (Zona z : empleado.getZonasAsignadas()) {
            System.out.println("- Zona " + z.getId());
        }
    }

    private void mostrarVehiculosPorZona(Empleado empleado, ArrayList<Zona> zonas) {
        if (empleado.getZonasAsignadas().isEmpty()) {
            System.out.println("No tiene zonas asignadas.");
            return;
        }

        for (Zona z : empleado.getZonasAsignadas()) {
            System.out.println("Zona " + z.getId() + ":");
            for (Garage g : z.getGarages()) {
                Vehiculo v = g.getVehiculo();
                if (v != null) {
                    System.out.println("  - Vehículo: " + v.getMatricula());
                }
            }
        }
    }

    private void asignarTrabajoVehiculo(Empleado empleado) {
        if (empleado.getVehiculosAsignados().isEmpty()) {
            System.out.println("No tiene Vehiculos asignados.");
            return;
        }

        Scanner input = new Scanner(System.in);
        System.out.println("Se te a asignado los Vehiculos .");
        for (Vehiculo v : empleado.getVehiculosAsignados()) {
            System.out.println("El Vehiculo: " + v.getMatricula());

        }
        System.out.println("Cual es el veiculo en el cual se travajo.");
        String matricula = input.nextLine();
        

        for (Vehiculo v : empleado.getVehiculosAsignados()) {
            if (v.getMatricula().equalsIgnoreCase(matricula)) {
                empleado.cargarVehiculo(v);
                empleado.lavarVehiculo(v);
                empleado.mantenerVehiculo(v);
            }
        }

    }
}
